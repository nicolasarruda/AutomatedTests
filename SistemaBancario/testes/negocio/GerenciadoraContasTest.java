package negocio2;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class GerenciadoraContasTest {

	private GerenciadoraContas gerContas;
	private int idConta01 = 1, idConta02 = 2, 
			idConta03 = 3, idConta04 = 4;
	
	@Before
	public void setUp() {
		
		/* ======= Montagem do cenário ========= */

		// criando algumas contas
		 ContaCorrente conta01 = new ContaCorrente(idConta01, 2000.0, true);
		 ContaCorrente conta02 = new ContaCorrente(idConta02, 0.0, true);
		 ContaCorrente conta03 = new ContaCorrente(idConta03, -500.0, true);
		 ContaCorrente conta04 = new ContaCorrente(idConta04, -1000.0, true);
		
		// inserindo as contas na lista de contas do banco
		List<ContaCorrente> contasDoBanco = new ArrayList<ContaCorrente>();
		contasDoBanco.add(conta01);
		contasDoBanco.add(conta02);
		contasDoBanco.add(conta03);
		contasDoBanco.add(conta04);
		
		gerContas = new GerenciadoraContas(contasDoBanco);
		
		// a) Abriu a conexão com BD? Então...
		// b) Criou arquivos e pastas aqui? Então...
		// c) Inseriu clientes fictícios na base de dados
		// especificamente para os testes desta classe? Então...
	}
	
	public void tearDown() {
		gerContas.limpa();
		
		// a) Fecha aqui!!!
		// b) Apaga todos eles aqui!!!
		// c) Apaga eles aqui!!!
	}
	
	/**
	 * Teste de transfêrencia de valores entre as contas.
	 * 
	 * @author Nícolas Luchini
	 */
	@Test
	public void testTransfereValor() {
		
		/*====== Execução ========*/
		 
		gerContas.transfereValor(idConta01, 500.0, idConta02);
		gerContas.transfereValor(idConta02, 200.0, idConta01);
		
		/*======= Verificações ======= */
		
		assertThat(gerContas.pesquisaConta(idConta02).getSaldo(), is(300.0));
		assertThat(gerContas.pesquisaConta(idConta01).getSaldo(), is(1700.0));
	}

	/**
	 * 
	 * Teste de saldo insuficiente.
	 * Regra de negócio mudou. Agora é permitido o envio de dinheiro
	 *  de uma conta com saldo negativo
	 * @author Nícolas Luchini
	 */
	
	
//	@Test
//	public void testTransfereValor_SaldoInsuficiente() {
//	*/
	
		/*======== Execução ========*/
//		ContaCorrente conta01 = gerContas.pesquisaConta(idConta01);
//		ContaCorrente conta02 = gerContas.pesquisaConta(idConta02);
		
//		boolean sucesso = gerContas.transfereValor(idConta01, 2500, idConta02);
		
		/*======== Verificações =======*/
		
//		assertTrue(sucesso);
//		assertThat(conta01.getSaldo(), is(-500.0));
//		assertThat(conta02.getSaldo(), is(2500.0));
//	}
	
	@Test
	public void testTransfereValor_SaldoNegativo() {
			
		/*======== Execução ========*/
		
		ContaCorrente conta02 = gerContas.pesquisaConta(idConta02);
		ContaCorrente conta03 = gerContas.pesquisaConta(idConta03);
		
		
		boolean sucesso = gerContas.transfereValor(idConta03, 200, idConta02);
		
		/*======== Verificações =======*/
		
		assertTrue(sucesso);
		assertThat(conta02.getSaldo(), is(200.0));
		assertThat(conta03.getSaldo(), is(-700.0));

	}
	
	@Test
	public void testTransfereValor_SaldoNegativoParaNegativo() {
			
        /*======== Execução ========*/
		
		ContaCorrente conta03 = gerContas.pesquisaConta(idConta03);
		ContaCorrente conta04 = gerContas.pesquisaConta(idConta04);
		
		boolean sucesso = gerContas.transfereValor(idConta04, 500, idConta03);
		
		/*======== Verificações =======*/
		
		assertTrue(sucesso);
		assertThat(conta03.getSaldo(), is(0.0));
		assertThat(conta04.getSaldo(), is(-1500.0));

	}
}
