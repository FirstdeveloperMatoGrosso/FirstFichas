import '../database.dart';

class TblIngressosTable extends SupabaseTable<TblIngressosRow> {
  @override
  String get tableName => 'tbl_ingressos';

  @override
  TblIngressosRow createRow(Map<String, dynamic> data) => TblIngressosRow(data);
}

class TblIngressosRow extends SupabaseDataRow {
  TblIngressosRow(Map<String, dynamic> data) : super(data);

  @override
  SupabaseTable get table => TblIngressosTable();

  int get id => getField<int>('id')!;
  set id(int value) => setField<int>('id', value);

  String? get nomeIngresso => getField<String>('nome_ingresso');
  set nomeIngresso(String? value) => setField<String>('nome_ingresso', value);

  double? get valor => getField<double>('valor');
  set valor(double? value) => setField<double>('valor', value);

  DateTime? get createdIt => getField<DateTime>('created_it');
  set createdIt(DateTime? value) => setField<DateTime>('created_it', value);
}
