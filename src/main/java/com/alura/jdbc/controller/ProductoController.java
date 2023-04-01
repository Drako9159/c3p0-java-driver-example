package com.alura.jdbc.controller;

import com.alura.jdbc.factory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {
	public int modificar(String nombre, String descripcion, Integer id) throws SQLException {
		final Connection con = new ConnectionFactory().recuperaConexion();
		try (con){
			final PreparedStatement statement = con.prepareStatement("UPDATE producto SET nombre= ? , descripcion= ? WHERE id= ? " );
			try (statement){
				statement.setString(1, nombre);
				statement.setString(2, descripcion);
				statement.setInt(3, id);
				statement.execute();
				return statement.getUpdateCount();
			}
		}
	}
	public int eliminar(Integer id) throws SQLException {
		final Connection con = new ConnectionFactory().recuperaConexion();
		try (con){
			final PreparedStatement statement = con.prepareStatement("DELETE FROM producto WHERE id = ? ");
			try (statement){
				statement.setInt(1, id);
				statement.execute();
				return statement.getUpdateCount();
			}
		}
	}

	public List<Map<String, String>> listar() throws SQLException {
		final Connection con = new ConnectionFactory().recuperaConexion();
		try (con){
			final PreparedStatement statement = con.prepareStatement("SELECT id, nombre, descripcion, cantidad FROM producto");
			try (statement){
				statement.execute();
				ResultSet resultSet = statement.getResultSet();
				List<Map<String, String>> resultado = new ArrayList<>();
				while (resultSet.next()){
					Map<String, String> fila = new HashMap<>();
					fila.put("id", String.valueOf(resultSet.getInt("id")));
					fila.put("nombre", resultSet.getString("nombre"));
					fila.put("descripcion", resultSet.getString("descripcion"));
					fila.put("cantidad", String.valueOf(resultSet.getInt("cantidad")));
					resultado.add(fila);
				}
				return resultado;
			}
		}
	}

    public void guardar(Map<String, String> producto) throws SQLException {
		String nombre = producto.get("nombre");
		String descripcion = producto.get("descripcion");
		Integer cantidad = Integer.valueOf(producto.get("cantidad"));
		Integer maximaCantidad = 50;

		final Connection con = new ConnectionFactory().recuperaConexion();
		try(con){
			con.setAutoCommit(false);
			final PreparedStatement statement = con.prepareStatement("INSERT INTO producto (nombre, descripcion, cantidad) "
							+ "VALUES ( ?, ?, ? )",
					Statement.RETURN_GENERATED_KEYS);
			try(statement){
				do {
					int cantidadParaGuardar = Math.min(cantidad, maximaCantidad);
					ejecutaRegistro(nombre, descripcion, String.valueOf(cantidadParaGuardar), statement);
					cantidad -= maximaCantidad;
				} while (cantidad > 0);
					// commit fon confirmed process
					con.commit();
			} catch (Exception e){
				// if error, resolve transaction
				con.rollback();
				System.out.println("ROLLBACK");
			}
		}
	}
	public void ejecutaRegistro(String nombre, String descripcion, String cantidad, PreparedStatement statement) throws SQLException {
		/*if (Integer.valueOf(cantidad) < 50){
			throw new RuntimeException("Ocurrió un error");
		}*/
		statement.setString(1, nombre);
		statement.setString(2, descripcion);
		statement.setString(3, cantidad);
		statement.execute();

		final ResultSet resultSet = statement.getGeneratedKeys();
		try (resultSet){
			while (resultSet.next()) {
				System.out.println(String.format("Fue insertado el producto ID %d", resultSet.getInt(1)));
			}
		}
	}
}
