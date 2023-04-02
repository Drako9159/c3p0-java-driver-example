package com.alura.jdbc.dao;

import com.alura.jdbc.factory.ConnectionFactory;
import com.alura.jdbc.modelo.Producto;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoDAO {
    final private Connection con;

    public ProductoDAO(Connection con) {
        this.con = con;
    }

    public void guardar(Producto producto) {
        // final Connection con = new ConnectionFactory().recuperaConexion();
        try (con) {
            //con.setAutoCommit(false);
            final PreparedStatement statement = con.prepareStatement("INSERT INTO producto (nombre, descripcion, cantidad, categoria_id) "
                            + "VALUES ( ?, ?, ?, ? )",
                    Statement.RETURN_GENERATED_KEYS);
            try (statement) {
                //do {
                //int cantidadParaGuardar = Math.min(cantidad, maximaCantidad);
                ejecutaRegistro(producto, statement);
                //cantidad -= maximaCantidad;
                //} while (cantidad > 0);
                // commit fon confirmed process
                //con.commit();
            }
        } catch (SQLException e) {
            // if error, resolve transaction
                /*
                con.rollback();
                System.out.println("ROLLBACK");*/
            throw new RuntimeException(e);
        }
    }

    public void ejecutaRegistro(Producto producto, PreparedStatement statement) throws SQLException {
        /*if (Integer.valueOf(cantidad) < 50){
            throw new RuntimeException("Ocurrió un error");
        }*/
        statement.setString(1, producto.getNombre());
        statement.setString(2, producto.getDescripcion());
        statement.setString(3, String.valueOf(producto.getCantidad()));
        statement.setInt(4, producto.getCategoriaId());
        statement.execute();

        final ResultSet resultSet = statement.getGeneratedKeys();
        try (resultSet) {
            while (resultSet.next()) {
                producto.setId(resultSet.getInt(1));
                System.out.println(String.format("Fue insertado el producto ID %s", producto));
            }
        }
    }

    public List<Producto> listar() {
        List<Producto> resultado = new ArrayList<>();
        try {
            final PreparedStatement statement = con.prepareStatement("SELECT id, nombre, descripcion, cantidad FROM producto");
            try (statement) {
                statement.execute();
                final ResultSet resultSet = statement.getResultSet();
                try (resultSet) {
                    while (resultSet.next()) {
                        Producto fila = new Producto(resultSet.getInt("id"),
                                resultSet.getString("nombre"),
                                resultSet.getString("descripcion"),
                                resultSet.getInt("cantidad"));
                        resultado.add(fila);
                    }
                }
                return resultado;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) {
        try {
            final PreparedStatement statement = con.prepareStatement("UPDATE producto SET nombre= ? , descripcion= ?, cantidad= ? WHERE id= ? ");
            try (statement) {
                statement.setString(1, nombre);
                statement.setString(2, descripcion);
                statement.setInt(3, cantidad);
                statement.setInt(4, id);
                statement.execute();
                return statement.getUpdateCount();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int eliminar(Integer id) {
        try {
            final PreparedStatement statement = con.prepareStatement("DELETE FROM producto WHERE id = ? ");
            try (statement) {
                statement.setInt(1, id);
                statement.execute();
                return statement.getUpdateCount();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Producto> listar(Integer categoriaId) {
        List<Producto> resultado = new ArrayList<>();
        try {
            var querySelect = "SELECT id, nombre, descripcion, cantidad FROM producto WHERE categoria_id = ?";
            System.out.println(querySelect);
            final PreparedStatement statement = con.prepareStatement(querySelect);
            try (statement) {
                statement.setInt(1, categoriaId);
                statement.execute();
                final ResultSet resultSet = statement.getResultSet();
                try (resultSet) {
                    while (resultSet.next()) {
                        Producto fila = new Producto(resultSet.getInt("id"),
                                resultSet.getString("nombre"),
                                resultSet.getString("descripcion"),
                                resultSet.getInt("cantidad"));
                        resultado.add(fila);
                    }
                }
                return resultado;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
