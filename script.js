// Array para armazenar as fichas
let fichas = [];

// Função para salvar uma nova ficha
document.getElementById('fichaForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const ficha = {
        id: Date.now(),
        nome: document.getElementById('nome').value,
        data: document.getElementById('data').value,
        descricao: document.getElementById('descricao').value
    };
    
    fichas.push(ficha);
    salvarFichasLocalStorage();
    atualizarListaFichas();
    this.reset();
});

// Função para salvar fichas no localStorage
function salvarFichasLocalStorage() {
    localStorage.setItem('fichas', JSON.stringify(fichas));
}

// Função para carregar fichas do localStorage
function carregarFichasLocalStorage() {
    const fichasSalvas = localStorage.getItem('fichas');
    if (fichasSalvas) {
        fichas = JSON.parse(fichasSalvas);
        atualizarListaFichas();
    }
}

// Função para atualizar a lista de fichas na tela
function atualizarListaFichas() {
    const listaFichas = document.getElementById('listaFichas');
    listaFichas.innerHTML = '';
    
    fichas.forEach(ficha => {
        const fichaElement = document.createElement('div');
        fichaElement.className = 'ficha-item';
        fichaElement.innerHTML = `
            <h3>${ficha.nome}</h3>
            <p>Data: ${formatarData(ficha.data)}</p>
            <p>${ficha.descricao}</p>
            <button onclick="excluirFicha(${ficha.id})">Excluir</button>
        `;
        listaFichas.appendChild(fichaElement);
    });
}

// Função para formatar a data
function formatarData(data) {
    return new Date(data).toLocaleDateString('pt-BR');
}

// Função para excluir uma ficha
function excluirFicha(id) {
    if (confirm('Tem certeza que deseja excluir esta ficha?')) {
        fichas = fichas.filter(ficha => ficha.id !== id);
        salvarFichasLocalStorage();
        atualizarListaFichas();
    }
}

// Carregar fichas ao iniciar a página
document.addEventListener('DOMContentLoaded', carregarFichasLocalStorage);
