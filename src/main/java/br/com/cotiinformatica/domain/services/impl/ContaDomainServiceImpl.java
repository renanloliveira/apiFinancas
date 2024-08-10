package br.com.cotiinformatica.domain.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.cotiinformatica.domain.models.dtos.ContaRequestDto;
import br.com.cotiinformatica.domain.models.dtos.ContaResponseDto;
import br.com.cotiinformatica.domain.models.entities.Conta;
import br.com.cotiinformatica.domain.models.enums.TipoConta;
import br.com.cotiinformatica.domain.services.interfaces.ContaDomainService;
import br.com.cotiinformatica.infrastructure.repositories.CategoriaRepository;
import br.com.cotiinformatica.infrastructure.repositories.ContaRepository;

@Service
public class ContaDomainServiceImpl implements ContaDomainService {

	@Autowired
	ContaRepository contaRepository;
	@Autowired
	CategoriaRepository categoriaRepository;
	@Autowired
	ModelMapper modelMapper;

	@Override
	public ContaResponseDto adicionar(ContaRequestDto request) throws Exception {

		// verificar se a categoria existe no banco de dados
		var categoria = categoriaRepository.findById(request.getCategoriaId())
				.orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));

		// capturando os dados da conta
		var conta = modelMapper.map(request, Conta.class);

		// relacionando a conta com a categoria
		conta.setCategoria(categoria);

		// salvando no banco de dados
		contaRepository.save(conta);

		// retornando os dados da resposta
		return modelMapper.map(conta, ContaResponseDto.class);

	}

	@Override
	public ContaResponseDto editar(Integer id, ContaRequestDto request) throws Exception {

		// consulatndo a conta no banco de dados através do ID
		var conta = contaRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Conta não encotrada."));

		// consulatndo a conta no banco de dados através do ID
		var categoria = categoriaRepository.findById(request.getCategoriaId())
				.orElseThrow(() -> new IllegalArgumentException("Categoria não encotrada."));

		conta.setNome(request.getNome());
		conta.setData(request.getData());
		conta.setValor(BigDecimal.valueOf(request.getValor()));
		conta.setTipo(TipoConta.valueOf(request.getTipo()));
		conta.setObservacoes(request.getObervacoes());
		conta.setCategoria(categoria);

		// salvando as alteracões no bancos de dados
		contaRepository.save(conta);

		// retornando os dodos da resposta
		return modelMapper.map(conta, ContaResponseDto.class);
	}

	@Override
	public ContaResponseDto excluir(Integer id) throws Exception {
		// consulatndo a conta no banco de dados através do ID
		var conta = contaRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Conta não encotrada."));

		// excluiindo a conta do banco de dados
		contaRepository.delete(conta);

		// retornando os dodos da resposta
		return modelMapper.map(conta, ContaResponseDto.class);
	}

	@Override
	public List<ContaResponseDto> consultar() throws Exception {

		// consultando a conta no banco de dados através do ID
		var response = contaRepository.findAll().stream().map((conta) -> modelMapper.map(conta, ContaResponseDto.class))
				.collect(Collectors.toList());

		// retornado os dados
		return response;
	}

	@Override
	public ContaResponseDto obterPorId(Integer id) throws Exception {

		// consulatndo a conta no banco de dados através do ID
		var conta = contaRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("Conta não encotrada."));

		// retornando os dodos da resposta
		return modelMapper.map(conta, ContaResponseDto.class);
	}

}
