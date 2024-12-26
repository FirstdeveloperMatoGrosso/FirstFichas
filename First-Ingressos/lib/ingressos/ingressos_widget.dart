import '/components/drop_dow_widget.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import '/flutter_flow/flutter_flow_count_controller.dart';
import '/flutter_flow/flutter_flow_drop_down.dart';
import '/flutter_flow/flutter_flow_expanded_image_view.dart';
import '/flutter_flow/flutter_flow_theme.dart';
import '/flutter_flow/flutter_flow_util.dart';
import '/flutter_flow/flutter_flow_widgets.dart';
import '/flutter_flow/form_field_controller.dart';
import 'dart:ui';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:page_transition/page_transition.dart';
import 'package:provider/provider.dart';
import 'ingressos_model.dart';
export 'ingressos_model.dart';

class IngressosWidget extends StatefulWidget {
  const IngressosWidget({Key? key}) : super(key: key);

  @override
  _IngressosWidgetState createState() => _IngressosWidgetState();
}

class _IngressosWidgetState extends State<IngressosWidget> {
  Future<List<dynamic>> fetchEventos() async {
    CollectionReference eventos = FirebaseFirestore.instance.collection('eventos');
    var list = [];
    await eventos.get().then((QuerySnapshot snapshot) {
      snapshot.docs.forEach((doc) {
        // print('${doc.id} => ${doc.data()}');
        list.add(doc.data());
      });
    }).catchError((error) => print("Failed to fetch eventos: $error"));
    return list;
  }

  var cart = [];

  late IngressosModel _model;

  final scaffoldKey = GlobalKey<ScaffoldState>();

  @override
  void initState() {
    super.initState();
    _model = createModel(context, () => IngressosModel());

    WidgetsBinding.instance.addPostFrameCallback((_) => setState(() {}));
  }

  @override
  void dispose() {
    _model.dispose();

    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    if (isiOS) {
      SystemChrome.setSystemUIOverlayStyle(
        SystemUiOverlayStyle(
          statusBarBrightness: Theme.of(context).brightness,
          systemStatusBarContrastEnforced: true,
        ),
      );
    }

    context.watch<FFAppState>();

    return GestureDetector(
      onTap: () => _model.unfocusNode.canRequestFocus
          ? FocusScope.of(context).requestFocus(_model.unfocusNode)
          : FocusScope.of(context).unfocus(),
      child: Scaffold(
        key: scaffoldKey,
        backgroundColor: FlutterFlowTheme.of(context).primaryBackground,
        body: Visibility(
          visible: responsiveVisibility(
            context: context,
            tabletLandscape: false,
            desktop: false,
          ),
          child: Align(
            alignment: AlignmentDirectional(0.0, 0.0),
            child: Padding(
              padding: EdgeInsetsDirectional.fromSTEB(0.0, 0.0, 0.0, 0.0),
              child: Container(
                width: double.infinity,
                constraints: BoxConstraints(
                  maxWidth: 500.0,
                ),
                decoration: BoxDecoration(
                  color: FlutterFlowTheme.of(context).primaryBackground,
                  image: DecorationImage(
                    fit: BoxFit.cover,
                    image: Image.asset(
                      'assets/images/IMG_4201.jpg',
                    ).image,
                  ),
                ),
                alignment: AlignmentDirectional(0.0, 0.0),
                child: Padding(
                  padding: EdgeInsetsDirectional.fromSTEB(0.0, 20.0, 0.0, 0.0),
                  child: ListView(
                    children: [
                      Row(
                        mainAxisSize: MainAxisSize.max,
                        children: [
                          Container(
                            width: 180.0,
                            height: 79.0,
                            decoration: BoxDecoration(
                              // image: DecorationImage(
                              //   fit: BoxFit.cover,
                              // image: Image.asset(
                              //   'assets/images/2vqf7_',
                              // ).image,
                              // ),
                            ),
                            child: InkWell(
                              splashColor: Colors.transparent,
                              focusColor: Colors.transparent,
                              hoverColor: Colors.transparent,
                              highlightColor: Colors.transparent,
                              onTap: () async {
                                await Navigator.push(
                                  context,
                                  PageTransition(
                                    type: PageTransitionType.fade,
                                    child: FlutterFlowExpandedImageView(
                                      image: Image.asset(
                                        'assets/images/fijek_4.png',
                                        fit: BoxFit.contain,
                                      ),
                                      allowRotation: false,
                                      tag: 'imageTag2',
                                      useHeroAnimation: true,
                                    ),
                                  ),
                                );
                              },
                              child: Hero(
                                tag: 'imageTag2',
                                transitionOnUserGestures: true,
                                child: ClipRRect(
                                  borderRadius: BorderRadius.circular(8.0),
                                  child: Image.asset(
                                    'assets/images/fijek_4.png',
                                    width: double.infinity,
                                    height: 105.0,
                                    fit: BoxFit.fitHeight,
                                  ),
                                ),
                              ),
                            ),
                          ),
                          Expanded(
                            child: Row(
                              mainAxisSize: MainAxisSize.max,
                              mainAxisAlignment: MainAxisAlignment.end,
                              children: [
                                Padding(
                                  padding: EdgeInsetsDirectional.fromSTEB(
                                      0.0, 0.0, 22.0, 0.0),
                                  child: InkWell(
                                    splashColor: Colors.transparent,
                                    focusColor: Colors.transparent,
                                    hoverColor: Colors.transparent,
                                    highlightColor: Colors.transparent,
                                    onTap: () async {
                                      context.pushNamed('login');
                                    },
                                    child: Container(
                                      width: 100.0,
                                      height: 48.0,
                                      decoration: BoxDecoration(
                                        borderRadius: BorderRadius.only(
                                          bottomLeft: Radius.circular(18.0),
                                          bottomRight: Radius.circular(18.0),
                                          topLeft: Radius.circular(18.0),
                                          topRight: Radius.circular(18.0),
                                        ),
                                        shape: BoxShape.rectangle,
                                        border: Border.all(
                                          color: FlutterFlowTheme.of(context)
                                              .primaryText,
                                        ),
                                      ),
                                      child: Row(
                                        mainAxisSize: MainAxisSize.max,
                                        mainAxisAlignment:
                                        MainAxisAlignment.center,
                                        children: [
                                          // Icon(
                                          //   FFIcons.kuser1,
                                          //   color: FlutterFlowTheme.of(context)
                                          //       .primaryText,
                                          //   size: 24.0,
                                          // ),
                                          Text(
                                            'SAIR',
                                            style: FlutterFlowTheme.of(context)
                                                .bodyMedium
                                                .override(
                                              fontFamily: 'Brazila',
                                              fontWeight: FontWeight.w600,
                                              useGoogleFonts: false,
                                            ),
                                          ),
                                        ],
                                      ),
                                    ),
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ],
                      ),
                      SingleChildScrollView(
                        child: Column(
                          mainAxisSize: MainAxisSize.max,
                          children: [
                            ClipRRect(
                              child: BackdropFilter(
                                filter: ImageFilter.blur(
                                  sigmaX: 7.0,
                                  sigmaY: 6.0,
                                ),
                                child: Align(
                                  alignment: AlignmentDirectional(0.0, 0.0),
                                  child: Padding(
                                    padding: EdgeInsetsDirectional.fromSTEB(
                                        10.0, 0.0, 10.0, 0.0),
                                    child: Column(
                                      mainAxisSize: MainAxisSize.max,
                                      crossAxisAlignment:
                                          CrossAxisAlignment.start,
                                      children: [
                                      FutureBuilder(
                                      future: fetchEventos(), // a previously-obtained Future<String> or null
                                      builder: (BuildContext context, AsyncSnapshot snapshot) {
                                        List<Widget> children = [];
                                        if (snapshot.hasData) {
                                            snapshot.data.forEach((doc) {
                                              if(doc['status'] == 'ativo') {
                                                children.add(
                                                    Card(
                                                      semanticContainer: true,
                                                      clipBehavior: Clip
                                                          .antiAliasWithSaveLayer,
                                                      child: Stack(
                                                          children: <Widget>[
                                                            ShaderMask(
                                                              child: FittedBox(
                                                                child: Image.network(doc['capa']),
                                                                fit: BoxFit.cover,
                                                              ),
                                                              shaderCallback: (Rect bounds) {
                                                                return LinearGradient(
                                                                  begin: Alignment.bottomCenter,
                                                                  end: Alignment.topCenter,
                                                                  colors: [Colors.black, Colors.transparent],
                                                                  stops: [
                                                                    0.0,
                                                                    0.6,
                                                                  ],
                                                                  tileMode: TileMode.mirror,
                                                                ).createShader(bounds);
                                                              },
                                                              blendMode: BlendMode.multiply,
                                                            ),
                                                            Padding(
                                                              padding: EdgeInsets
                                                                  .only(
                                                                  top: 165.0,
                                                                  left: 6.0,
                                                                  right: 6.0,
                                                                  bottom: 6.0),
                                                              child: ExpansionTile(
                                                                title: Text(
                                                                    doc['nome'],
                                                                    style: TextStyle(fontSize: 30, color: Colors.white),
                                                                ),
                                                                children: <Widget>[
                                                              Padding(
                                                                  padding: EdgeInsets
                                                                      .only(
                                                                      top: 20.0,
                                                                      left: 6.0,
                                                                      right: 6.0,
                                                                      bottom: 6.0),
                                                                  child: Column(
                                                                    children: <Widget>[
                                                                      Text(
                                                                        doc['descricao'],
                                                                        style: TextStyle(fontSize: 22, color: Colors.black),
                                                                      ),
                                                                      Row(
                                                                        mainAxisSize: MainAxisSize.max,
                                                                        mainAxisAlignment:
                                                                        MainAxisAlignment.start,
                                                                        children: [
                                                                          Padding(
                                                                            padding: EdgeInsetsDirectional
                                                                                .fromSTEB(
                                                                                0.0, 0.0, 0.0, 5.0),
                                                                            child: Icon(
                                                                              Icons.location_on_outlined,
                                                                              color: Colors.black,
                                                                              size: 24.0,
                                                                            ),
                                                                          ),
                                                                          Text(
                                                                            doc['local'],
                                                                            textAlign: TextAlign.start,
                                                                            style: TextStyle(color: Colors.black),
                                                                          ),
                                                                        ],
                                                                      ),
                                                                      Row(
                                                                        mainAxisSize: MainAxisSize.max,
                                                                        children: [
                                                                          Padding(
                                                                            padding: EdgeInsetsDirectional
                                                                                .fromSTEB(0.0, 0.0, 5.0, 5.0),
                                                                            child: Icon(
                                                                              Icons.edit_calendar,
                                                                              color: Colors.black,
                                                                              size: 20.0,
                                                                            ),
                                                                          ),
                                                                          Text(
                                                                            'Classificação' + doc['classificacao'].toString(),
                                                                            style: TextStyle(color: Colors.black),
                                                                          ),
                                                                        ],
                                                                      ),
                                                                      Row(
                                                                        mainAxisSize: MainAxisSize.max,
                                                                        children: [
                                                                          Padding(
                                                                            padding: EdgeInsetsDirectional
                                                                                .fromSTEB(0.0, 0.0, 5.0, 5.0),
                                                                            child: Icon(
                                                                              Icons.edit_calendar,
                                                                              color: Colors.black,
                                                                              size: 20.0,
                                                                            ),
                                                                          ),
                                                                          Text(
                                                                            'Taxa' + doc['taxa'].toString(),
                                                                            style: TextStyle(color: Colors.black),
                                                                          ),
                                                                        ],
                                                                      ),
                                                                      Row(
                                                                        mainAxisSize: MainAxisSize.max,
                                                                        children: [
                                                                          Padding(
                                                                            padding: EdgeInsetsDirectional
                                                                                .fromSTEB(0.0, 0.0, 5.0, 5.0),
                                                                            child: Icon(
                                                                              Icons.edit_calendar,
                                                                              color: Colors.black,
                                                                              size: 20.0,
                                                                            ),
                                                                          ),
                                                                          Text(
                                                                            'Valor' + doc['valor'].toString(),
                                                                            style: TextStyle(color: Colors.black),
                                                                          ),
                                                                        ],
                                                                      ),
                                                                      Row(
                                                                        mainAxisSize: MainAxisSize.max,
                                                                        children: [
                                                                          Padding(
                                                                            padding: EdgeInsetsDirectional
                                                                                .fromSTEB(0.0, 0.0, 5.0, 5.0),
                                                                            child: Icon(
                                                                              Icons.edit_calendar,
                                                                              color: Colors.black,
                                                                              size: 20.0,
                                                                            ),
                                                                          ),
                                                                          Text(
                                                                            doc['data'] + ' | ' + doc['hora'],
                                                                            style: TextStyle(color: Colors.black),
                                                                          ),
                                                                        ],
                                                                      ),
                                                                  Row(
                                                                    mainAxisSize: MainAxisSize
                                                                        .max,
                                                                    mainAxisAlignment: MainAxisAlignment
                                                                        .center,
                                                                    children: [
                                                                      Align(
                                                                        alignment: AlignmentDirectional(
                                                                            0.0,
                                                                            1.0),
                                                                        child: Padding(
                                                                          padding: EdgeInsetsDirectional
                                                                              .fromSTEB(
                                                                              0.0,
                                                                              20.0,
                                                                              0.0,
                                                                              20.0),
                                                                          child: FFButtonWidget(
                                                                            onPressed: () {
                                                                              cart.add(doc);
                                                                            },
                                                                            text: 'Adicionar no carrinho',
                                                                            icon: Icon(
                                                                              Icons
                                                                                  .add_shopping_cart,
                                                                              size: 15.0,
                                                                            ),
                                                                            options: FFButtonOptions(
                                                                              width: 200.0,
                                                                              height: 60.0,
                                                                              padding: EdgeInsets
                                                                                  .all(
                                                                                  10.0),
                                                                              iconPadding:
                                                                              EdgeInsetsDirectional
                                                                                  .fromSTEB(
                                                                                  0.0,
                                                                                  0.0,
                                                                                  0.0,
                                                                                  0.0),
                                                                              color: FlutterFlowTheme
                                                                                  .of(
                                                                                  context)
                                                                                  .success,
                                                                              textStyle: FlutterFlowTheme
                                                                                  .of(
                                                                                  context)
                                                                                  .titleSmall
                                                                                  .override(
                                                                                fontFamily: 'Brazila',
                                                                                color: Colors
                                                                                    .white,
                                                                                useGoogleFonts: false,
                                                                              ),
                                                                              elevation: 10.0,
                                                                              borderSide: BorderSide(
                                                                                color: Colors
                                                                                    .transparent,
                                                                                width: 1.0,
                                                                              ),
                                                                              borderRadius:
                                                                              BorderRadius
                                                                                  .circular(
                                                                                  20.0),
                                                                            ),
                                                                          ),
                                                                        ),
                                                                      ),
                                                                    ],
                                                                  ),
                                                                    ]
                                                                  ),

                                                              )
                                                                ]
                                                              ),
                                                            ),
                                                          ]
                                                      ),
                                                      shape: RoundedRectangleBorder(
                                                        borderRadius: BorderRadius
                                                            .circular(10.0),
                                                      ),
                                                      elevation: 5,
                                                      margin: EdgeInsets.all(
                                                          10),
                                                    )
                                                );
                                              }
                                            });
                                            children.add(
                                              FFButtonWidget(
                                                onPressed: (){
                                                  context.pushNamed('carrinho',
                                                    extra: <String, dynamic>{
                                                      kTransitionInfoKey: TransitionInfo(
                                                      hasTransition: true,
                                                      transitionType: PageTransitionType.fade,
                                                      ),
                                                      'extra': cart
                                                    },
                                                  );
                                                },
                                                text: 'Finalizar',
                                                options: FFButtonOptions(
                                                  width: 200.0,
                                                  height: 60.0,
                                                  padding: EdgeInsets
                                                      .all(
                                                      10.0),
                                                  color: FlutterFlowTheme
                                                      .of(
                                                      context)
                                                      .success,
                                                  textStyle: FlutterFlowTheme
                                                      .of(
                                                      context)
                                                      .titleSmall
                                                      .override(
                                                    fontFamily: 'Brazila',
                                                    color: Colors
                                                        .white,
                                                    useGoogleFonts: false,
                                                  ),
                                                  elevation: 10.0,
                                                  borderSide: BorderSide(
                                                    color: Colors
                                                        .transparent,
                                                    width: 1.0,
                                                  ),
                                                  borderRadius:
                                                  BorderRadius
                                                      .circular(
                                                      20.0),
                                                ),
                                              ),
                                            );
                                            return Column(
                                              children: children
                                            );
                                        } else if (snapshot.hasError) {
                                          children = <Widget>[
                                            const Icon(
                                              Icons.error_outline,
                                              color: Colors.red,
                                              size: 60,
                                            ),
                                            Padding(
                                              padding: const EdgeInsets.only(top: 16),
                                              child: Text('Error: ${snapshot.error}'),
                                            ),
                                          ];
                                        } else {
                                          children = const <Widget>[
                                            SizedBox(
                                              width: 60,
                                              height: 60,
                                              child: CircularProgressIndicator(),
                                            ),
                                            Padding(
                                              padding: EdgeInsets.only(top: 16),
                                              child: Text('Awaiting result...'),
                                            ),
                                          ];
                                        }
                                        return Center(
                                          child: Column(
                                            mainAxisAlignment: MainAxisAlignment.center,
                                            children: children,
                                          ),
                                        );
                                      },
                                    ),
                                      ],
                                    ),
                                  ),
                                ),
                              ),
                            ),
                          ]
                              .divide(SizedBox(height: 20.0))
                              .addToEnd(SizedBox(height: 10.0)),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
