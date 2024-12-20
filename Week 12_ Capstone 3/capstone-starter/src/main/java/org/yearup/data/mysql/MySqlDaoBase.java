package org.yearup.data.mysql;

import org.yearup.models.ShoppingCart;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class MySqlDaoBase
{
    public DataSource dataSource;

    public MySqlDaoBase(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    public abstract ShoppingCart addingItems(int userId, int productId);

    public abstract void deleteCart(int userId);

    protected Connection getConnection() throws SQLException
    {
        return dataSource.getConnection();
    }
}
