import '/flutter_flow/flutter_flow_animations.dart';
import '/flutter_flow/flutter_flow_theme.dart';
import '/flutter_flow/flutter_flow_util.dart';
import '/flutter_flow/flutter_flow_widgets.dart';
import 'package:flutter/material.dart';
import 'package:flutter/scheduler.dart';
import 'package:flutter/services.dart';
import 'package:flutter_animate/flutter_animate.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:provider/provider.dart';
import 'carrinho_model.dart';
export 'carrinho_model.dart';

class CarrinhoWidget extends StatefulWidget {
  const CarrinhoWidget({Key? key}) : super(key: key);

  @override
  _CarrinhoWidgetState createState() => _CarrinhoWidgetState();
}

class _CarrinhoWidgetState extends State<CarrinhoWidget>
    with TickerProviderStateMixin {
  late CarrinhoModel _model;

  final scaffoldKey = GlobalKey<ScaffoldState>();
  var hasColumnTriggered = false;
  final animationsMap = {
    'buttonOnPageLoadAnimation': AnimationInfo(
      trigger: AnimationTrigger.onPageLoad,
      effects: [
        FadeEffect(
          curve: Curves.easeInOut,
          delay: 0.ms,
          duration: 600.ms,
          begin: 0.0,
          end: 1.0,
        ),
        MoveEffect(
          curve: Curves.easeInOut,
          delay: 0.ms,
          duration: 600.ms,
          begin: Offset(38.0, 0.0),
          end: Offset(0.0, 0.0),
        ),
      ],
    ),
    'columnOnPageLoadAnimation': AnimationInfo(
      trigger: AnimationTrigger.onPageLoad,
      applyInitialState: false,
      effects: [
        FadeEffect(
          curve: Curves.easeInOut,
          delay: 0.ms,
          duration: 600.ms,
          begin: 0.0,
          end: 1.0,
        ),
        MoveEffect(
          curve: Curves.easeInOut,
          delay: 0.ms,
          duration: 600.ms,
          begin: Offset(62.0, 0.0),
          end: Offset(0.0, 0.0),
        ),
      ],
    ),
    'columnOnActionTriggerAnimation': AnimationInfo(
      trigger: AnimationTrigger.onActionTrigger,
      applyInitialState: false,
      effects: [
        FadeEffect(
          curve: Curves.easeInOut,
          delay: 0.ms,
          duration: 600.ms,
          begin: 0.0,
          end: 1.0,
        ),
        MoveEffect(
          curve: Curves.easeInOut,
          delay: 0.ms,
          duration: 600.ms,
          begin: Offset(-63.0, 0.0),
          end: Offset(0.0, 0.0),
        ),
      ],
    ),
    'circleImageOnPageLoadAnimation': AnimationInfo(
      loop: true,
      trigger: AnimationTrigger.onPageLoad,
      effects: [
        FadeEffect(
          curve: Curves.easeInOut,
          delay: 0.ms,
          duration: 1270.ms,
          begin: 0.0,
          end: 1.0,
        ),
      ],
    ),
    'textOnPageLoadAnimation': AnimationInfo(
      loop: true,
      trigger: AnimationTrigger.onPageLoad,
      effects: [
        FadeEffect(
          curve: Curves.easeInOut,
          delay: 0.ms,
          duration: 1430.ms,
          begin: 0.0,
          end: 1.0,
        ),
      ],
    ),
    'containerOnPageLoadAnimation': AnimationInfo(
      trigger: AnimationTrigger.onPageLoad,
      effects: [
        FadeEffect(
          curve: Curves.easeInOut,
          delay: 230.ms,
          duration: 600.ms,
          begin: 0.0,
          end: 1.0,
        ),
      ],
    ),
  };

  @override
  void initState() {
    super.initState();
    _model = createModel(context, () => CarrinhoModel());

    setupAnimations(
      animationsMap.values.where((anim) =>
          anim.trigger == AnimationTrigger.onActionTrigger ||
          !anim.applyInitialState),
      this,
    );

    SchedulerBinding.instance.addPostFrameCallback((_) {
      animationsMap['columnOnPageLoadAnimation']!.controller.forward(from: 0.0);
    });

    WidgetsBinding.instance.addPostFrameCallback((_) => setState(() {}));
  }

  @override
  void dispose() {
    _model.dispose();

    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    var total = 0;

    final Map<String, dynamic> extra = GoRouterState.of(context).extra! as Map<String, dynamic>;

    final Future fetchCarrinho = Future.delayed(
      const Duration(seconds: 2),
          () => extra['extra'],
    );

    calcTotal() {
      for(int i=0; i<extra['extra'].length; i++){
        // total = total + extra['extra'][i]['valor'];
      }
    }

    if (isiOS) {
      SystemChrome.setSystemUIOverlayStyle(
        SystemUiOverlayStyle(
          statusBarBrightness: Theme.of(context).brightness,
          systemStatusBarContrastEnforced: true,
        ),
      );
    }

    context.watch<FFAppState>();

    return Scaffold(
      key: scaffoldKey,
      backgroundColor: Colors.black,
      body: Stack(
        children: [
          Align(
            alignment: AlignmentDirectional(0.0, 0.0),
            child: ClipRRect(
              borderRadius: BorderRadius.circular(8.0),
              child: Image.asset(
                'assets/images/IMG_4201.jpg',
                width: 1510.0,
                height: 1512.0,
                fit: BoxFit.cover,
              ),
            ),
          ),
          if (responsiveVisibility(
            context: context,
            tablet: false,
            tabletLandscape: false,
            desktop: false,
          ))
            Container(
              width: double.infinity,
              height: double.infinity,
              child: Column(
                children: [
                  Padding(
                    padding:
                        EdgeInsetsDirectional.fromSTEB(10.0, 50.0, 10.0, 0.0),
                    child: SingleChildScrollView(
                      child: Column(
                        mainAxisSize: MainAxisSize.max,
                        children: [
                          Padding(
                            padding: EdgeInsetsDirectional.fromSTEB(
                                10.0, 0.0, 10.0, 0.0),
                            child: Row(
                              mainAxisSize: MainAxisSize.max,
                              mainAxisAlignment: MainAxisAlignment.end,
                              children: [
                                Padding(
                                  padding: EdgeInsetsDirectional.fromSTEB(
                                      0.0, 0.0, 0.0, 0.0),
                                  child: FFButtonWidget(
                                    onPressed: () {
                                      extra['extra'] = [];
                                      calcTotal();
                                    },
                                    text: 'Limpar',
                                    options: FFButtonOptions(
                                      height: 40.0,
                                      padding: EdgeInsetsDirectional.fromSTEB(
                                          10.0, 0.0, 10.0, 0.0),
                                      iconPadding:
                                          EdgeInsetsDirectional.fromSTEB(
                                              0.0, 0.0, 0.0, 0.0),
                                      color: Color(0xFFE13C27),
                                      textStyle: FlutterFlowTheme.of(context)
                                          .titleSmall
                                          .override(
                                            fontFamily: 'Brazila',
                                            color: Colors.white,
                                            fontSize: 15.0,
                                            useGoogleFonts: false,
                                          ),
                                      elevation: 3.0,
                                      borderSide: BorderSide(
                                        color: FlutterFlowTheme.of(context)
                                            .lineColor,
                                        width: 1.0,
                                      ),
                                      borderRadius: BorderRadius.circular(12.0),
                                    ),
                                  )//.animateOnPageLoad(animationsMap[
                                      //'buttonOnPageLoadAnimation']!),
                                ),
                              ],
                            ),
                          ),
                          Padding(
                            padding: EdgeInsetsDirectional.fromSTEB(
                                0.0, 10.0, 0.0, 0.0),
                            child: FutureBuilder(
                              future: fetchCarrinho, // a previously-obtained Future<String> or null
                              builder: (BuildContext context, AsyncSnapshot snapshot) {
                                List<Widget> children = [];
                                if (snapshot.hasData) {
                                  snapshot.data.forEach((doc) {
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
                                                                              extra['extra'].remove(doc);
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
                                  });
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
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ),
        ],
      ),
    );
  }
}
