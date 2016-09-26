package br.edu.fa7.teste_db_xml;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TesteDBXML {
	
	private static Connection conexao;
	private static int numeroDeRegistros = 100000;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		conexao = createConnection();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if(conexao != null) {
			conexao.close();
		}
	}

	public static Connection createConnection() throws SQLException{
		String url = "jdbc:postgresql://localhost:5432/xml_db";
		String user = "postgres";
		String password = "postgres";
		
		Connection conexao = null;
		conexao = DriverManager.getConnection(url, user, password);
		
		return conexao;
	}
	
	@Test
	public void deveInserirRegistros() throws SQLException {
		PreparedStatement preparedStatement = null;
		String query = "INSERT INTO produto (id, data) VALUES (?, xml(?))";
		try {
			List<String> produtos = GeradorDeXML.gerarXML(numeroDeRegistros);
			long tempoinicial = System.currentTimeMillis();
			preparedStatement = conexao.prepareStatement(query);
			for(int i=0; i<produtos.size(); i++) {
				String xml = produtos.get(i);
				preparedStatement.setInt(1, i+1);
				preparedStatement.setString(2, xml);
				preparedStatement.executeUpdate();
			}
			long tempoToatal = (System.currentTimeMillis() - tempoinicial)/1000;

			System.out.println(String.format("%s Registros inseridos em %s segundos", numeroDeRegistros, tempoToatal));

		} catch (SQLException e) {
			System.out.println(e.getMessage());

		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		}
	}
	
	
	@Test
	public void deveDeletarRegistros() throws SQLException {
		PreparedStatement preparedStatement = null;
		String query = "DELETE FROM produto where id=?";
		try {
			List<String> produtos = GeradorDeXML.gerarXML(numeroDeRegistros);
			long tempoinicial = System.currentTimeMillis();
			preparedStatement = conexao.prepareStatement(query);
			for(int i=1; i<=produtos.size(); i++) {
				preparedStatement.setInt(1, i);
				preparedStatement.executeUpdate();
			}
			long tempoToatal = (System.currentTimeMillis() - tempoinicial)/1000;
			System.out.println(String.format("%s Registros deletados em %s segundos", numeroDeRegistros, tempoToatal));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		}
	}
	
	@Test
	public void deveAtualizarRegistros() throws SQLException {
		PreparedStatement preparedStatement = null;
		String query = "update produto set data=(SELECT xmlconcat((select data from produto where id=?), '<fornecedor><nome>Fornecedor 1</nome></fornecedor>')) where id=?";
		try {
			List<String> produtos = GeradorDeXML.gerarXML(numeroDeRegistros);
			long tempoinicial = System.currentTimeMillis();
			preparedStatement = conexao.prepareStatement(query);
			for(int i=1; i<=produtos.size(); i++) {
				preparedStatement.setInt(1, i);
				preparedStatement.setInt(2, i);
				preparedStatement.executeUpdate();
			}
			long tempoToatal = (System.currentTimeMillis() - tempoinicial)/1000;
			System.out.println(String.format("%s Alterador deletados em %s segundos", numeroDeRegistros, tempoToatal));
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		}
	}

}
