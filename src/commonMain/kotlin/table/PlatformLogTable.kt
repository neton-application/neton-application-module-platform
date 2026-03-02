package table

import model.PlatformLog
import model.PlatformLogTableImpl
import neton.database.api.Table

object PlatformLogTable : Table<PlatformLog, Long> by PlatformLogTableImpl
