import 'dart:async';

import 'package:collection/collection.dart';

import '/backend/schema/util/firestore_util.dart';
import '/backend/schema/util/schema_util.dart';

import 'index.dart';
import '/flutter_flow/flutter_flow_util.dart';

class CarrinhoRecord extends FirestoreRecord {
  CarrinhoRecord._(
    DocumentReference reference,
    Map<String, dynamic> data,
  ) : super(reference, data) {
    _initializeFields();
  }

  // "nome" field.
  String? _nome;
  String get nome => _nome ?? '';
  bool hasNome() => _nome != null;

  // "useriD" field.
  String? _useriD;
  String get useriD => _useriD ?? '';
  bool hasUseriD() => _useriD != null;

  // "promotor" field.
  String? _promotor;
  String get promotor => _promotor ?? '';
  bool hasPromotor() => _promotor != null;

  // "ingresso" field.
  String? _ingresso;
  String get ingresso => _ingresso ?? '';
  bool hasIngresso() => _ingresso != null;

  // "quanty" field.
  int? _quanty;
  int get quanty => _quanty ?? 0;
  bool hasQuanty() => _quanty != null;

  // "valor" field.
  double? _valor;
  double get valor => _valor ?? 0.0;
  bool hasValor() => _valor != null;

  DocumentReference get parentReference => reference.parent.parent!;

  void _initializeFields() {
    _nome = snapshotData['nome'] as String?;
    _useriD = snapshotData['useriD'] as String?;
    _promotor = snapshotData['promotor'] as String?;
    _ingresso = snapshotData['ingresso'] as String?;
    _quanty = castToType<int>(snapshotData['quanty']);
    _valor = castToType<double>(snapshotData['valor']);
  }

  static Query<Map<String, dynamic>> collection([DocumentReference? parent]) =>
      parent != null
          ? parent.collection('carrinho')
          : FirebaseFirestore.instance.collectionGroup('carrinho');

  static DocumentReference createDoc(DocumentReference parent) =>
      parent.collection('carrinho').doc();

  static Stream<CarrinhoRecord> getDocument(DocumentReference ref) =>
      ref.snapshots().map((s) => CarrinhoRecord.fromSnapshot(s));

  static Future<CarrinhoRecord> getDocumentOnce(DocumentReference ref) =>
      ref.get().then((s) => CarrinhoRecord.fromSnapshot(s));

  static CarrinhoRecord fromSnapshot(DocumentSnapshot snapshot) =>
      CarrinhoRecord._(
        snapshot.reference,
        mapFromFirestore(snapshot.data() as Map<String, dynamic>),
      );

  static CarrinhoRecord getDocumentFromData(
    Map<String, dynamic> data,
    DocumentReference reference,
  ) =>
      CarrinhoRecord._(reference, mapFromFirestore(data));

  @override
  String toString() =>
      'CarrinhoRecord(reference: ${reference.path}, data: $snapshotData)';

  @override
  int get hashCode => reference.path.hashCode;

  @override
  bool operator ==(other) =>
      other is CarrinhoRecord &&
      reference.path.hashCode == other.reference.path.hashCode;
}

Map<String, dynamic> createCarrinhoRecordData({
  String? nome,
  String? useriD,
  String? promotor,
  String? ingresso,
  int? quanty,
  double? valor,
}) {
  final firestoreData = mapToFirestore(
    <String, dynamic>{
      'nome': nome,
      'useriD': useriD,
      'promotor': promotor,
      'ingresso': ingresso,
      'quanty': quanty,
      'valor': valor,
    }.withoutNulls,
  );

  return firestoreData;
}

class CarrinhoRecordDocumentEquality implements Equality<CarrinhoRecord> {
  const CarrinhoRecordDocumentEquality();

  @override
  bool equals(CarrinhoRecord? e1, CarrinhoRecord? e2) {
    return e1?.nome == e2?.nome &&
        e1?.useriD == e2?.useriD &&
        e1?.promotor == e2?.promotor &&
        e1?.ingresso == e2?.ingresso &&
        e1?.quanty == e2?.quanty &&
        e1?.valor == e2?.valor;
  }

  @override
  int hash(CarrinhoRecord? e) => const ListEquality().hash(
      [e?.nome, e?.useriD, e?.promotor, e?.ingresso, e?.quanty, e?.valor]);

  @override
  bool isValidKey(Object? o) => o is CarrinhoRecord;
}
