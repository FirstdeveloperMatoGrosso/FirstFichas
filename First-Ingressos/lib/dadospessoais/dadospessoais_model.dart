import '/auth/firebase_auth/auth_util.dart';
import '/flutter_flow/flutter_flow_animations.dart';
import '/flutter_flow/flutter_flow_theme.dart';
import '/flutter_flow/flutter_flow_util.dart';
import '/flutter_flow/flutter_flow_widgets.dart';
import 'dadospessoais_widget.dart' show DadospessoaisWidget;
import 'package:flutter/material.dart';
import 'package:flutter/scheduler.dart';
import 'package:flutter/services.dart';
import 'package:flutter_animate/flutter_animate.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';

class DadospessoaisModel extends FlutterFlowModel<DadospessoaisWidget> {
  ///  State fields for stateful widgets in this page.

  // State field(s) for textNome widget.
  FocusNode? textNomeFocusNode;
  TextEditingController? textNomeController;
  String? Function(BuildContext, String?)? textNomeControllerValidator;
  // State field(s) for textSobrenome widget.
  FocusNode? textSobrenomeFocusNode;
  TextEditingController? textSobrenomeController;
  String? Function(BuildContext, String?)? textSobrenomeControllerValidator;
  // State field(s) for textCPF widget.
  FocusNode? textCPFFocusNode;
  TextEditingController? textCPFController;
  String? Function(BuildContext, String?)? textCPFControllerValidator;
  // State field(s) for textEmail widget.
  FocusNode? textEmailFocusNode;
  TextEditingController? textEmailController;
  String? Function(BuildContext, String?)? textEmailControllerValidator;
  // State field(s) for ddd widget.
  FocusNode? dddFocusNode;
  TextEditingController? dddController;
  String? Function(BuildContext, String?)? dddControllerValidator;
  // State field(s) for numeroCelular widget.
  FocusNode? numeroCelularFocusNode;
  TextEditingController? numeroCelularController;
  String? Function(BuildContext, String?)? numeroCelularControllerValidator;
  // State field(s) for country widget.
  FocusNode? countryFocusNode;
  TextEditingController? countryController;
  String? Function(BuildContext, String?)? countryControllerValidator;
  // State field(s) for estadoSigla widget.
  FocusNode? estadoSiglaFocusNode;
  TextEditingController? estadoSiglaController;
  String? Function(BuildContext, String?)? estadoSiglaControllerValidator;
  // State field(s) for cidade widget.
  FocusNode? cidadeFocusNode;
  TextEditingController? cidadeController;
  String? Function(BuildContext, String?)? cidadeControllerValidator;
  // State field(s) for cep widget.
  FocusNode? cepFocusNode;
  TextEditingController? cepController;
  String? Function(BuildContext, String?)? cepControllerValidator;
  // State field(s) for numeroCasa widget.
  FocusNode? numeroCasaFocusNode;
  TextEditingController? numeroCasaController;
  String? Function(BuildContext, String?)? numeroCasaControllerValidator;
  // State field(s) for bairro widget.
  FocusNode? bairroFocusNode;
  TextEditingController? bairroController;
  String? Function(BuildContext, String?)? bairroControllerValidator;
  // State field(s) for rua widget.
  FocusNode? ruaFocusNode;
  TextEditingController? ruaController;
  String? Function(BuildContext, String?)? ruaControllerValidator;

  /// Initialization and disposal methods.

  void initState(BuildContext context) {}

  void dispose() {
    textNomeFocusNode?.dispose();
    textNomeController?.dispose();

    textSobrenomeFocusNode?.dispose();
    textSobrenomeController?.dispose();

    textCPFFocusNode?.dispose();
    textCPFController?.dispose();

    textEmailFocusNode?.dispose();
    textEmailController?.dispose();

    dddFocusNode?.dispose();
    dddController?.dispose();

    numeroCelularFocusNode?.dispose();
    numeroCelularController?.dispose();

    countryFocusNode?.dispose();
    countryController?.dispose();

    estadoSiglaFocusNode?.dispose();
    estadoSiglaController?.dispose();

    cidadeFocusNode?.dispose();
    cidadeController?.dispose();

    cepFocusNode?.dispose();
    cepController?.dispose();

    numeroCasaFocusNode?.dispose();
    numeroCasaController?.dispose();

    bairroFocusNode?.dispose();
    bairroController?.dispose();

    ruaFocusNode?.dispose();
    ruaController?.dispose();
  }

  /// Action blocks are added here.

  /// Additional helper methods are added here.
}
