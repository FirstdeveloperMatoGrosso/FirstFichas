import '../database.dart';

class TblPromotorTable extends SupabaseTable<TblPromotorRow> {
  @override
  String get tableName => 'tbl_promotor';

  @override
  TblPromotorRow createRow(Map<String, dynamic> data) => TblPromotorRow(data);
}

class TblPromotorRow extends SupabaseDataRow {
  TblPromotorRow(Map<String, dynamic> data) : super(data);

  @override
  SupabaseTable get table => TblPromotorTable();

  int get id => getField<int>('id')!;
  set id(int value) => setField<int>('id', value);

  DateTime? get createdAt => getField<DateTime>('created_at');
  set createdAt(DateTime? value) => setField<DateTime>('created_at', value);

  String? get nomePromotor => getField<String>('nome_promotor');
  set nomePromotor(String? value) => setField<String>('nome_promotor', value);

  int? get idIngresso => getField<int>('id_ingresso');
  set idIngresso(int? value) => setField<int>('id_ingresso', value);
}
