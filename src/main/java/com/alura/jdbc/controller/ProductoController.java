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

	public void eliminar(Integer id) {
		// TODO
	}

	public List<Map<String, String>> listar() throws SQLException {
		// TODO
		/*
		Connection con = DriverManager.getConnection(
				"jdbc:mysql://localhost/control_de_stock?useTimeZone=true&serverTimeZone=UTC",
				"root",
				"password");*/
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

    public void guardar(Object producto) {
		// TODO
	}

}
