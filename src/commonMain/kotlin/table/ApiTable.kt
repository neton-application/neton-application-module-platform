package table

import model.Api
import model.ApiTableImpl
import neton.database.api.Table

object ApiTable : Table<Api, Long> by ApiTableImpl
