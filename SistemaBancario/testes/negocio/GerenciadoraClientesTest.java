package negocio2;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import negocio.Cliente;
import negocio.GerenciadoraClientes;
import negocio.IdadeNaoPermitidaException;

public class GerenciadoraClientesTest {
	
	private GerenciadoraClientes gerClientes;
	private int idCliente01 = 1, idCliente02 = 2, idCliente03 = 3;
	
	@Before
	public void setUp() {
		
		/* ======== Montagem do cene�rio ========= */
	
		// criando alguns clientes
		Cliente cliente01 = new Cliente(idCliente01, "N�colas Arruda", 18, "nlarruda@hotmail.com", 0, true);
		Cliente cliente02 = new Cliente(idCliente02, "N�colas Luchini", 25, "nla25@hotmail.com", 0, true);

		// inserindo clientes na lista do banco
		List<Cliente> clientesDoBanco = new ArrayList<>();
		clientesDoBanco.add(cliente01);
		clientesDoBanco.add(cliente02);
		
		gerClientes = new GerenciadoraClientes(clientesDoBanco);
		
		// a) Abriu a conex�o com BD? Ent�o...
		// b) Criou arquivos e pastas aqui? Ent�o...
		// c) Inseriu clientes fict�cios na base de dados
		// especificamente para os testes desta classe? Ent�o...
	}
	
	@After
	public void tearDown() {
		gerClientes.limpa();
		// a) Fecha aqui!!!
		// b) Apaga todos eles aqui!!!
		// c) Apaga eles aqui!!!
	}
	
	/**
	 * Teste de consulta de um cliente cadastrado no banco.
	 * 
	 *  @author N�colas
	 */
	@Test
	public void testPesquisaCliente() {

		// 2 parte de teste: execu��o
		
		Cliente cliente = gerClientes.pesquisaCliente(idCliente01);
		
		// 3 parte de teste: verifica��o
		
		assertThat(cliente.getId(), is(idCliente01));
		assertThat(cliente.getEmail(), is("nlarruda@hotmail.com"));
	}
	
	/**
	 * Teste de consulta de um cliente inexistente no banco.
	 * 
	 *  @author N�colas
	 */
	@Test
	public void testPesquisaClienteInexistente() {

		// 2 parte de teste: execu��o
		
		Cliente cliente = gerClientes.pesquisaCliente(1001);
		
		// 3 parte de teste: verifica��o
		assertNull(cliente);
		
	}
	
	/**
	 * Teste de remo��o de um cliente.
	 * 
	 * @author N�colas Luchini
	 */
	@Test
	public void testRemoveCliente() {

		/*========== Execu��o ===============*/
		
		boolean clienteRemovido = gerClientes.removeCliente(idCliente01);
		
		/*=========== Verifica��es ===========*/
		
		assertThat(clienteRemovido, is(true));
		assertThat(gerClientes.getClientesDoBanco().size(), is(idCliente01));
		assertNull(gerClientes.pesquisaCliente(idCliente01));
		
	}
	
	/**
	 * Teste de remo��o de um cliente inexistente.
	 * 
	 * @author N�colas Luchini
	 */
	@Test
	public void testRemoveClienteInexistente() {

		/*========== Execu��o ===============*/
		
		boolean clienteRemovido = gerClientes.removeCliente(1001);
		
		/*=========== Verifica��es ===========*/
		
		assertThat(clienteRemovido, is(false));
		assertThat(gerClientes.getClientesDoBanco().size(), is(2));
	}
	
	// ================== Testes de Limite ==================
	
	/**
	 * Valida��o da idade de um cliente que est� dentro do intervalo permitido 
	 * @author N�colas
	 * @throws IdadeNaoPermitidaException
	 */
	@Test
	public void testIdadeClienteAceitavel_01() throws IdadeNaoPermitidaException {
		
		/* ========= Montagem do Cen�rio ====== */
		
		Cliente cliente = new Cliente(idCliente03, "Lucas", 18, "lucas@gmail.com", 1, true);
		
		/*========== Execu��o ===============*/
		
		boolean idadeValida_18 = gerClientes.validaIdade(cliente.getIdade());
		
		/*=========== Verifica��es ===========*/
		
		assertTrue(idadeValida_18);
	}
	
	/**
	 * Valida��o da idade de um cliente que est� dentro do intervalo permitido 
	 * @author N�colas
	 * @throws IdadeNaoPermitidaException
	 */
	@Test
	public void testIdadeClienteAceitavel_02() throws IdadeNaoPermitidaException {
		
		/* ========= Montagem do Cen�rio ====== */
		
		Cliente cliente = new Cliente(idCliente03, "Lucas", 65, "lucas@gmail.com", 1, true);
		
		/*========== Execu��o ===============*/
		
		boolean idadeValida_65 = gerClientes.validaIdade(cliente.getIdade());
		
		/*=========== Verifica��es ===========*/
		
		assertTrue(idadeValida_65);
		
	}
	
	/**
	 * Valida��o da idade de um cliente que est� dentro do intervalo permitido 
	 * @author N�colas
	 * @throws IdadeNaoPermitidaException
	 */
	@Test
	public void testIdadeClienteAceitavel_03() throws IdadeNaoPermitidaException {
		
		/* ========= Montagem do Cen�rio ====== */
		
		Cliente cliente = new Cliente(idCliente03, "Lucas", 19, "lucas@gmail.com", 1, true);
		
		/*========== Execu��o ===============*/
		
		boolean idadePermitida = gerClientes.validaIdade(cliente.getIdade());

		
		/*=========== Verifica��es ===========*/
		
		assertTrue(idadePermitida);
	}
	
	/**
	 * Invalida��o do cadastro de um cliente que est� fora do intervalo de idade permitida
	 * @author N�colas
	 * @throws IdadeNaoPermitidaException
	 */
	@Test
	public void testIdadeClienteInaceitavel_01() throws IdadeNaoPermitidaException {
		
		/* ========= Montagem do Cen�rio ====== */
		
		Cliente cliente = new Cliente(idCliente03, "Lucas", 17, "lucas@gmail.com", 1, true);
		
		/*========== Execu��o ===============*/
		try {
			gerClientes.validaIdade(cliente.getIdade());
			fail();
		} catch (Exception e) {
			/*=========== Verifica��es ===========*/
			assertThat(e.getMessage(), is(IdadeNaoPermitidaException.MSG_IDADE_INVALIDA));
		}
	}
	
	/**
	 * Invalida��o do cadastro de um cliente que est� fora do intervalo de idade permitida
	 * @author N�colas
	 * @throws IdadeNaoPermitidaException
	 */
	@Test
	public void testIdadeClienteInaceitavel_02() throws IdadeNaoPermitidaException {
		
		/* ========= Montagem do Cen�rio ====== */
		
		Cliente cliente = new Cliente(idCliente03, "Lucas", 66, "lucas@gmail.com", 1, true);
		
		/*========== Execu��o ===============*/
		try {
			gerClientes.validaIdade(cliente.getIdade());
			fail();
		} catch (Exception e) {
			/*=========== Verifica��es ===========*/
			assertThat(e.getMessage(), is(IdadeNaoPermitidaException.MSG_IDADE_INVALIDA));
		}
	}
	
	// ============== Termina aqui testes de Limite ===========================================
	
}
