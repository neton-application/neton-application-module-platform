package table

import model.Client
import model.ClientTableImpl
import neton.database.api.Table

object ClientTable : Table<Client, Long> by ClientTableImpl
