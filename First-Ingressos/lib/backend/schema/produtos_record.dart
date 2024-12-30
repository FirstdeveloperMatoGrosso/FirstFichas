import 'dart:async';

import 'package:collection/collection.dart';

import '/backend/schema/util/firestore_util.dart';
import '/backend/schema/util/schema_util.dart';

import 'index.dart';
import '/flutter_flow/flutter_flow_util.dart';

class ProdutosRecord extends FirestoreRecord {
  ProdutosRecord._(
    DocumentReference reference,
    Map<String, dynamic> data,
  ) : super(reference, data) {
    _initializeFields();
  }

  // "nome" field.
  String? _nome;
  String get nome => _nome ?? '';
  bool hasNome() => _nome != null;

  // "descricao" field.
  String? _descricao;
  String get descricao => _descricao ?? '';
  bool hasDescricao() => _descricao != null;

  // "promotor" field.
  String? _promotor;
  String get promotor => _promotor ?? '';
  bool hasPromotor() => _promotor != null;

  // "area_ingresso" field.
  String? _areaIngresso;
  String get areaIngresso => _areaIngresso ?? '';
  bool hasAreaIngresso() => _areaIngresso != null;

  // "loca_ingresso" field.
  String? _locaIngresso;
  String get locaIngresso => _locaIngresso ?? '';
  bool hasLocaIngresso() => _locaIngresso != null;

  // "categoria" field.
  String? _categoria;
  String get categoria => _categoria ?? '';
  bool hasCategoria() => _categoria != null;

  // "preco" field.
  double? _preco;
  double get preco => _preco ?? 0.0;
  bool hasPreco() => _preco != null;

  // "quantidade" field.
  int? _quantidade;
  int get quantidade => _quantidade ?? 0;
  bool hasQuantidade() => _quantidade != null;

  // "imagem" field.
  String? _imagem;
  String get imagem => _imagem ?? '';
  bool hasImagem() => _imagem != null;

  // "data_engresso" field.
  DateTime? _dataEngresso;
  DateTime? get dataEngresso => _dataEngresso;
  bool hasDataEngresso() => _dataEngresso != null;

  void _initializeFields() {
    _nome = snapshotData['nome'] as String?;
    _descricao = snapshotData['descricao'] as String?;
    _promotor = snapshotData['promotor'] as String?;
    _areaIngresso = snapshotData['area_ingresso'] as String?;
    _locaIngresso = snapshotData['loca_ingresso'] as String?;
    _categoria = snapshotData['categoria'] as String?;
    _preco = castToType<double>(snapshotData['preco']);
    _quantidade = castToType<int>(snapshotData['quantidade']);
    _imagem = snapshotData['imagem'] as String?;
    _dataEngresso = snapshotData['data_engresso'] as DateTime?;
  }

  static CollectionReference get collection =>
      FirebaseFirestore.instance.collection('produtos');

  static Stream<ProdutosRecord> getDocument(DocumentReference ref) =>
      ref.snapshots().map((s) => ProdutosRecord.fromSnapshot(s));

  static Future<ProdutosRecord> getDocumentOnce(DocumentReference ref) =>
      ref.get().then((s) => ProdutosRecord.fromSnapshot(s));

  static ProdutosRecord fromSnapshot(DocumentSnapshot snapshot) =>
      ProdutosRecord._(
        snapshot.reference,
        mapFromFirestore(snapshot.data() as Map<String, dynamic>),
      );

  static ProdutosRecord getDocumentFromData(
    Map<String, dynamic> data,
    DocumentReference reference,
  ) =>
      ProdutosRecord._(reference, mapFromFirestore(data));

  @override
  String toString() =>
      'ProdutosRecord(reference: ${reference.path}, data: $snapshotData)';

  @override
  int get hashCode => reference.path.hashCode;

  @override
  bool operator ==(other) =>
      other is ProdutosRecord &&
      reference.path.hashCode == other.reference.path.hashCode;
}

Map<String, dynamic> createProdutosRecordData({
  String? nome,
  String? descricao,
  String? promotor,
  String? areaIngresso,
  String? locaIngresso,
  String? categoria,
  double? preco,
  int? quantidade,
  String? imagem,
  DateTime? dataEngresso,
}) {
  final firestoreData = mapToFirestore(
    <String, dynamic>{
      'nome': nome,
      'descricao': descricao,
      'promotor': promotor,
      'area_ingresso': areaIngresso,
      'loca_ingresso': locaIngresso,
      'categoria': categoria,
      'preco': preco,
      'quantidade': quantidade,
      'imagem': imagem,
      'data_engresso': dataEngresso,
    }.withoutNulls,
  );

  return firestoreData;
}

class ProdutosRecordDocumentEquality implements Equality<ProdutosRecord> {
  const ProdutosRecordDocumentEquality();

  @override
  bool equals(ProdutosRecord? e1, ProdutosRecord? e2) {
    return e1?.nome == e2?.nome &&
        e1?.descricao == e2?.descricao &&
        e1?.promotor == e2?.promotor &&
        e1?.areaIngresso == e2?.areaIngresso &&
        e1?.locaIngresso == e2?.locaIngresso &&
        e1?.categoria == e2?.categoria &&
        e1?.preco == e2?.preco &&
        e1?.quantidade == e2?.quantidade &&
        e1?.imagem == e2?.imagem &&
        e1?.dataEngresso == e2?.dataEngresso;
  }

  @override
  int hash(ProdutosRecord? e) => const ListEquality().hash([
        e?.nome,
        e?.descricao,
        e?.promotor,
        e?.areaIngresso,
        e?.locaIngresso,
        e?.categoria,
        e?.preco,
        e?.quantidade,
        e?.imagem,
        e?.dataEngresso
      ]);

  @override
  bool isValidKey(Object? o) => o is ProdutosRecord;
}
