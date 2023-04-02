package com.alura.jdbc.dao;

import com.alura.jdbc.modelo.Producto;

import java.sql.*;

public class ProductoDAO {
    final private Connection con;
    public ProductoDAO(Connection con){
        this.con = con;
    }
    public void guardar(Producto producto) throws SQLException {
       // final Connection con = new ConnectionFactory().recuperaConexion();
        try (con) {
            con.setAutoCommit(false);
            final PreparedStatement statement = con.prepareStatement("INSERT INTO producto (nombre, descripcion, cantidad) "
                            + "VALUES ( ?, ?, ? )",
                    Statement.RETURN_GENERATED_KEYS);
            try (statement) {
                //do {
                //int cantidadParaGuardar = Math.min(cantidad, maximaCantidad);
                ejecutaRegistro(producto, statement);
                //cantidad -= maximaCantidad;
                //} while (cantidad > 0);
                // commit fon confirmed process
                con.commit();
            } catch (Exception e) {
                // if error, resolve transaction
                e.printStackTrace();
                con.rollback();
                System.out.println("ROLLBACK");
            }

        }
    }

    public void ejecutaRegistro(Producto producto, PreparedStatement statement) throws SQLException {
		/*if (Integer.valueOf(cantidad) < 50){
			throw new RuntimeException("Ocurrió un error");
		}*/
        statement.setString(1, producto.getNombre());
        statement.setString(2, producto.getDescripcion());
        statement.setString(3, String.valueOf(producto.getCantidad()));
        statement.execute();

        final ResultSet resultSet = statement.getGeneratedKeys();
        try (resultSet){
            while (resultSet.next()) {
                producto.setId(resultSet.getInt(1));
                System.out.println(String.format("Fue insertado el producto ID %s", producto));
            }
        }
    }
}
