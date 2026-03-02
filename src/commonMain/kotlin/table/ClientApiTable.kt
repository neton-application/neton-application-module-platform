package table

import model.ClientApi
import model.ClientApiTableImpl
import neton.database.api.Table

object ClientApiTable : Table<ClientApi, Long> by ClientApiTableImpl
