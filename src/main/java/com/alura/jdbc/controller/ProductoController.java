package com.alura.jdbc.controller;

import com.alura.jdbc.factory.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoController {

	public void modificar(String nombre, String descripcion, Integer id) {
		// TODO
	}

	public int eliminar(Integer id) throws SQLException {
		Connection con = new ConnectionFactory().recuperaConexion();
		Statement statement = con.createStatement();
		statement.execute("DELETE FROM producto WHERE id = "+ id);
		return statement.getUpdateCount();

	}

	public List<Map<String, String>> listar() throws SQLException {

		Connection con = new ConnectionFactory().recuperaConexion();
		Statement statement = con.createStatement();
		statement.execute("SELECT id, nombre, descripcion, cantidad FROM producto");
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
		con.close();
		return resultado;
	}

    public void guardar(Map<String, String> producto) throws SQLException{
		Connection con = new ConnectionFactory().recuperaConexion();
		Statement statement = con.createStatement();
		statement.execute("INSERT INTO producto (nombre, descripcion, cantidad) "
				+ "VALUES ( '"+producto.get("nombre")+ "','"
				+ producto.get("descripcion")+ "',"
				+ producto.get("cantidad")+")", Statement.RETURN_GENERATED_KEYS);

		ResultSet resultSet = statement.getGeneratedKeys();
		while(resultSet.next()){
			System.out.println(String.format("Fue insertado el producto ID %d", resultSet.getInt(1)));

		}
		// TODO
	}

}
