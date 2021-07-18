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
		
		/* ======== Montagem do ceneário ========= */
	
		// criando alguns clientes
		Cliente cliente01 = new Cliente(idCliente01, "Nícolas Arruda", 18, "nlarruda@hotmail.com", 0, true);
		Cliente cliente02 = new Cliente(idCliente02, "Nícolas Luchini", 25, "nla25@hotmail.com", 0, true);

		// inserindo clientes na lista do banco
		List<Cliente> clientesDoBanco = new ArrayList<>();
		clientesDoBanco.add(cliente01);
		clientesDoBanco.add(cliente02);
		
		gerClientes = new GerenciadoraClientes(clientesDoBanco);
		
		// a) Abriu a conexão com BD? Então...
		// b) Criou arquivos e pastas aqui? Então...
		// c) Inseriu clientes fictícios na base de dados
		// especificamente para os testes desta classe? Então...
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
	 *  @author Nícolas
	 */
	@Test
	public void testPesquisaCliente() {

		// 2 parte de teste: execução
		
		Cliente cliente = gerClientes.pesquisaCliente(idCliente01);
		
		// 3 parte de teste: verificação
		
		assertThat(cliente.getId(), is(idCliente01));
		assertThat(cliente.getEmail(), is("nlarruda@hotmail.com"));
	}
	
	/**
	 * Teste de consulta de um cliente inexistente no banco.
	 * 
	 *  @author Nícolas
	 */
	@Test
	public void testPesquisaClienteInexistente() {

		// 2 parte de teste: execução
		
		Cliente cliente = gerClientes.pesquisaCliente(1001);
		
		// 3 parte de teste: verificação
		assertNull(cliente);
		
	}
	
	/**
	 * Teste de remoção de um cliente.
	 * 
	 * @author Nícolas Luchini
	 */
	@Test
	public void testRemoveCliente() {

		/*========== Execução ===============*/
		
		boolean clienteRemovido = gerClientes.removeCliente(idCliente01);
		
		/*=========== Verificações ===========*/
		
		assertThat(clienteRemovido, is(true));
		assertThat(gerClientes.getClientesDoBanco().size(), is(idCliente01));
		assertNull(gerClientes.pesquisaCliente(idCliente01));
		
	}
	
	/**
	 * Teste de remoção de um cliente inexistente.
	 * 
	 * @author Nícolas Luchini
	 */
	@Test
	public void testRemoveClienteInexistente() {

		/*========== Execução ===============*/
		
		boolean clienteRemovido = gerClientes.removeCliente(1001);
		
		/*=========== Verificações ===========*/
		
		assertThat(clienteRemovido, is(false));
		assertThat(gerClientes.getClientesDoBanco().size(), is(2));
	}
	
	// ================== Testes de Limite ==================
	
	/**
	 * Validação da idade de um cliente que está dentro do intervalo permitido 
	 * @author Nícolas
	 * @throws IdadeNaoPermitidaException
	 */
	@Test
	public void testIdadeClienteAceitavel_01() throws IdadeNaoPermitidaException {
		
		/* ========= Montagem do Cenário ====== */
		
		Cliente cliente = new Cliente(idCliente03, "Lucas", 18, "lucas@gmail.com", 1, true);
		
		/*========== Execução ===============*/
		
		boolean idadeValida_18 = gerClientes.validaIdade(cliente.getIdade());
		
		/*=========== Verificações ===========*/
		
		assertTrue(idadeValida_18);
	}
	
	/**
	 * Validação da idade de um cliente que está dentro do intervalo permitido 
	 * @author Nícolas
	 * @throws IdadeNaoPermitidaException
	 */
	@Test
	public void testIdadeClienteAceitavel_02() throws IdadeNaoPermitidaException {
		
		/* ========= Montagem do Cenário ====== */
		
		Cliente cliente = new Cliente(idCliente03, "Lucas", 65, "lucas@gmail.com", 1, true);
		
		/*========== Execução ===============*/
		
		boolean idadeValida_65 = gerClientes.validaIdade(cliente.getIdade());
		
		/*=========== Verificações ===========*/
		
		assertTrue(idadeValida_65);
		
	}
	
	/**
	 * Validação da idade de um cliente que está dentro do intervalo permitido 
	 * @author Nícolas
	 * @throws IdadeNaoPermitidaException
	 */
	@Test
	public void testIdadeClienteAceitavel_03() throws IdadeNaoPermitidaException {
		
		/* ========= Montagem do Cenário ====== */
		
		Cliente cliente = new Cliente(idCliente03, "Lucas", 19, "lucas@gmail.com", 1, true);
		
		/*========== Execução ===============*/
		
		boolean idadePermitida = gerClientes.validaIdade(cliente.getIdade());

		
		/*=========== Verificações ===========*/
		
		assertTrue(idadePermitida);
	}
	
	/**
	 * Invalidação do cadastro de um cliente que está fora do intervalo de idade permitida
	 * @author Nícolas
	 * @throws IdadeNaoPermitidaException
	 */
	@Test
	public void testIdadeClienteInaceitavel_01() throws IdadeNaoPermitidaException {
		
		/* ========= Montagem do Cenário ====== */
		
		Cliente cliente = new Cliente(idCliente03, "Lucas", 17, "lucas@gmail.com", 1, true);
		
		/*========== Execução ===============*/
		try {
			gerClientes.validaIdade(cliente.getIdade());
			fail();
		} catch (Exception e) {
			/*=========== Verificações ===========*/
			assertThat(e.getMessage(), is(IdadeNaoPermitidaException.MSG_IDADE_INVALIDA));
		}
	}
	
	/**
	 * Invalidação do cadastro de um cliente que está fora do intervalo de idade permitida
	 * @author Nícolas
	 * @throws IdadeNaoPermitidaException
	 */
	@Test
	public void testIdadeClienteInaceitavel_02() throws IdadeNaoPermitidaException {
		
		/* ========= Montagem do Cenário ====== */
		
		Cliente cliente = new Cliente(idCliente03, "Lucas", 66, "lucas@gmail.com", 1, true);
		
		/*========== Execução ===============*/
		try {
			gerClientes.validaIdade(cliente.getIdade());
			fail();
		} catch (Exception e) {
			/*=========== Verificações ===========*/
			assertThat(e.getMessage(), is(IdadeNaoPermitidaException.MSG_IDADE_INVALIDA));
		}
	}
	
	// ============== Termina aqui testes de Limite ===========================================
	
}
