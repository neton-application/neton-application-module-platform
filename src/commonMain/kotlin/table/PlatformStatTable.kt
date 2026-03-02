package table

import model.PlatformStat
import model.PlatformStatTableImpl
import neton.database.api.Table

object PlatformStatTable : Table<PlatformStat, Long> by PlatformStatTableImpl
