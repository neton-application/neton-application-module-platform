package table

import model.ChargeRecord
import model.ChargeRecordTableImpl
import neton.database.api.Table

object ChargeRecordTable : Table<ChargeRecord, Long> by ChargeRecordTableImpl
