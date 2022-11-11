package com.cs240.familymapserver.dao;

import java.sql.Connection;
import java.sql.SQLException;

interface Transaction {
    void run(Connection c) throws SQLException;
}

