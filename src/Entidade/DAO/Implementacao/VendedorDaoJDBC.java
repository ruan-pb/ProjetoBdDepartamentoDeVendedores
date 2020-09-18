package Entidade.DAO.Implementacao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DAO.VendedorDao;
import Entidades.Departamento;
import Entidades.Vendedor;
import db.DB;
import db.DbIntegridadeException;

public class VendedorDaoJDBC implements VendedorDao {
	
	private Connection conn;
	
	public VendedorDaoJDBC(Connection conexao) {
		this.conn = conexao;
	}

	@Override
	public void inserir(Vendedor departamento) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Vendedor departamento) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteId(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Vendedor findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName "+
										"FROM seller INNER JOIN department "+
										 "ON seller.DepartmentId = department.Id "+
										"WHERE seller.Id = ?");
			st.setInt(1, id);
			rs = st.executeQuery();
			if(rs.next()) {
				Departamento dp = instanciacaoDepartamento(rs);
				Vendedor vd = instanciacaoVendedor(rs, dp);
				
				return vd;
				
				
			}
			return null;
		}
		catch(SQLException e) {
			throw new DbIntegridadeException(e.getMessage());
		}
		finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
		
	}
	private Departamento instanciacaoDepartamento(ResultSet rs ) throws SQLException {
		Departamento dp = new Departamento();
		dp.setId(rs.getInt("DepartmentId"));
		dp.setNome(rs.getString("DepName"));
		return dp;
	}
	
	private Vendedor instanciacaoVendedor(ResultSet rs,Departamento dp) throws SQLException {
		Vendedor vd = new Vendedor();
		vd.setId(rs.getInt("Id"));
		vd.setNome(rs.getString("Name"));
		vd.setEmail(rs.getString("Email"));
		vd.setSalario(rs.getDouble("BaseSalary"));
		vd.setDataDeAnivers�rio(rs.getDate("BirthDate"));
		vd.setDepartamento(dp);
		return vd;
	}

	@Override
	public List<Vendedor> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Vendedor> findByDepartamento(Departamento departamento) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("SELECT seller.*,department.Name as DepName "+
					 "FROM seller INNER JOIN department"+
					" ON seller.DepartmentId = department.Id"+
					" WHERE DepartmentId = ?"+
					" ORDER BY Name");
			
			st.setInt(1, departamento.getId());
			rs = st.executeQuery();
			List<Vendedor> lista = new ArrayList<>();
			Map<Integer,Departamento>map = new HashMap<Integer, Departamento>();
			while(rs.next()) {
				
				Departamento dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					dep = instanciacaoDepartamento(rs);
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Vendedor vd = instanciacaoVendedor(rs, dep);
				
				lista.add(vd);
				
				
			}
			return lista;
		}
		catch(SQLException e) {
			throw new DbIntegridadeException(e.getMessage());
		}
		finally {
			DB.fecharResultSet(rs);
			DB.fecharStatement(st);
		}
	}
	

}
