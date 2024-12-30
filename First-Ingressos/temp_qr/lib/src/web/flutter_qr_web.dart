// ignore_for_file: avoid_web_libraries_in_flutter

import 'dart:async';
import 'dart:core';
import 'dart:html' as html;
import 'dart:js_util';
import 'dart:ui' as ui;

import 'package:flutter/material.dart';

import '../../qr_code_scanner.dart';
import 'jsqr.dart';
import 'media.dart';

/// Even though it has been highly modified, the original implementation has been
/// adopted from https://github.com:treeder/jsqr_flutter
///
/// Copyright 2020 @treeder
/// Copyright 2021 The one with the braid

class WebQrView extends StatefulWidget {
  final QRViewCreatedCallback onPlatformViewCreated;
  final PermissionSetCallback? onPermissionSet;
  final CameraFacing? cameraFacing;

  const WebQrView({
    Key? key,
    required this.onPlatformViewCreated,
    this.onPermissionSet,
    this.cameraFacing = CameraFacing.front,
  }) : super(key: key);

  @override
  _WebQrViewState createState() => _WebQrViewState();

  static html.DivElement vidDiv =
      html.DivElement(); // need a global for the registerViewFactory

  static Future<bool> cameraAvailable() async {
    final sources =
        await html.window.navigator.mediaDevices!.enumerateDevices();
    var hasCam = false;
    for (final e in sources) {
      if (e.kind == 'videoinput') {
        hasCam = true;
      }
    }
    return hasCam;
  }
}

class _WebQrViewState extends State<WebQrView> {
  html.MediaStream? _localStream;
  bool _currentlyProcessing = false;

  QRViewControllerWeb? _controller;

  late Size _size = const Size(0, 0);
  Timer? timer;
  String? code;
  String? _errorMsg;
  html.VideoElement video = html.VideoElement();
  String viewID = 'QRVIEW-' + DateTime.now().millisecondsSinceEpoch.toString();

  final StreamController<Barcode> _scanUpdateController =
      StreamController<Barcode>();
  late CameraFacing facing;

  Timer? _frameInterval;

  @override
  void initState() {
    super.initState();

    facing = widget.cameraFacing ?? CameraFacing.front;

    WebQrView.vidDiv.children = [video];
    // ignore: UNDEFINED_PREFIXED_NAME
    ui.platformViewRegistry
        .registerViewFactory(viewID, (int id) => WebQrView.vidDiv);
    // giving JavaScript some time to process the DOM changes
    Timer(const Duration(milliseconds: 500), () {
      start();
    });
  }

  Future<void> start() async {
    await _makeCall();
    _frameInterval?.cancel();
    _frameInterval =
        Timer.periodic(const Duration(milliseconds: 200), (timer) {
      _captureFrame2();
    });
  }

  void cancel() {
    _frameInterval?.cancel();
    _localStream?.getTracks().forEach((track) => track.stop());
    _localStream = null;
  }

  @override
  void dispose() {
    cancel();
    super.dispose();
  }

  Future<void> _makeCall() async {
    try {
      _localStream = await getUserMedia(facing);
      video
        ..srcObject = _localStream
        ..autoplay = true;
    } catch (e) {
      _errorMsg = e.toString();
    }
  }

  void _stopStream() {
    try {
      if (_localStream != null) {
        _localStream!.getTracks().forEach((track) => track.stop());
      }
      setState(() {
        _localStream = null;
      });
    } catch (e) {
      _errorMsg = e.toString();
    }
  }

  Future<void> _captureFrame2() async {
    if (_currentlyProcessing || _localStream == null) {
      return;
    }

    _currentlyProcessing = true;
    try {
      final code = await jsQR(video);
      if (code != null) {
        _scanUpdateController.add(
          Barcode(code, BarcodeFormat.qrcode, null),
        );
      }
    } catch (e) {
      _errorMsg = e.toString();
    } finally {
      _currentlyProcessing = false;
    }
  }

  @override
  Widget build(BuildContext context) {
    if (_errorMsg != null) {
      return Center(child: Text('Error: $_errorMsg'));
    }

    return LayoutBuilder(
      builder: (context, constraints) {
        _setCanvasSize(Size(constraints.maxWidth, constraints.maxHeight));
        return HtmlElementView(viewType: viewID);
      },
    );
  }

  void _setCanvasSize(Size size) {
    if (_size != size) {
      _size = size;
      video
        ..width = size.width.toInt()
        ..height = size.height.toInt();
    }
  }
}

class QRViewControllerWeb implements QRViewController {
  final _WebQrViewState _state;

  QRViewControllerWeb(this._state);

  @override
  void dispose() => _state.cancel();

  @override
  Future<CameraFacing> flipCamera() async {
    try {
      _state.facing = _state.facing == CameraFacing.front
          ? CameraFacing.back
          : CameraFacing.front;
      await _state.start();
      return _state.facing;
    } catch (e) {
      throw Exception('Failed to flip camera: $e');
    }
  }

  @override
  Future<CameraFacing> getCameraInfo() async {
    return _state.facing;
  }

  @override
  Future<bool?> getFlashStatus() async {
    // Flash is not supported in web browsers
    return false;
  }

  @override
  Future<SystemFeatures> getSystemFeatures() async {
    final hasCam = await WebQrView.cameraAvailable();
    return SystemFeatures(false, hasCam, hasCam);
  }

  @override
  bool get hasPermissions {
    try {
      return _state._localStream != null;
    } catch (e) {
      return false;
    }
  }

  @override
  Future<void> pauseCamera() async {
    try {
      _state._frameInterval?.cancel();
      final tracks = _state._localStream?.getVideoTracks();
      if (tracks != null) {
        for (final track in tracks) {
          track.enabled = false;
        }
      }
    } catch (e) {
      throw Exception('Failed to pause camera: $e');
    }
  }

  @override
  Future<void> resumeCamera() async {
    try {
      final tracks = _state._localStream?.getVideoTracks();
      if (tracks != null) {
        for (final track in tracks) {
          track.enabled = true;
        }
      }
      _state._frameInterval?.cancel();
      _state._frameInterval = Timer.periodic(
        const Duration(milliseconds: 200),
        (timer) => _state._captureFrame2(),
      );
    } catch (e) {
      throw Exception('Failed to resume camera: $e');
    }
  }

  @override
  Stream<Barcode> get scannedDataStream => _state._scanUpdateController.stream;

  @override
  Future<void> stopCamera() async {
    try {
      _state._frameInterval?.cancel();
      final tracks = _state._localStream?.getVideoTracks();
      if (tracks != null) {
        for (final track in tracks) {
          track.stop();
        }
      }
      _state._localStream?.getTracks().forEach((track) => track.stop());
      _state._localStream = null;
    } catch (e) {
      throw Exception('Failed to stop camera: $e');
    }
  }

  @override
  Future<void> toggleFlash() async {
    // Flash is not supported in web browsers
    return;
  }

  @override
  Future<void> scanInvert(bool isScanInvert) async {
    // Scan inversion is not supported in web implementation
    return;
  }
}

Widget createWebQrView({
  required QRViewCreatedCallback onPlatformViewCreated,
  PermissionSetCallback? onPermissionSet,
  CameraFacing? cameraFacing,
}) =>
    WebQrView(
      onPlatformViewCreated: onPlatformViewCreated,
      onPermissionSet: onPermissionSet,
      cameraFacing: cameraFacing,
    );
