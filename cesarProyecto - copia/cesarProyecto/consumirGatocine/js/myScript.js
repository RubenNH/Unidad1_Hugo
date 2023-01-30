fetch("http://localhost:3000/api/pais/")
    .then((response) => response.json())
    .then((data) => {
        console.log(data);
        obtenerPelis(data);
    })
    .catch((error) => {
        console.log(error);
    });


const obtenerPelis = (peliculas) => {
    const contenido = document.getElementById("idPais");
    peliculas.forEach((peli) => {
        contenido.innerHTML = `
        ${contenido.innerHTML}
        <option value="${peli.idPais}">${peli.pais}</option>
        `;
    });
};

const obtenerPeli = (peliculas) => {
    const contenido = document.getElementById("idEstados");
    peliculas.forEach((peli) => {
        contenido.innerHTML = `
        ${contenido.innerHTML}
        <option value="${peli.idEstados}">${peli.estados}</option>
        `;
    });
};

const obtenerPel = (peliculas) => {
    const contenido = document.getElementById("idMunicipios");
    peliculas.forEach((peli) => {
        contenido.innerHTML = `
        ${contenido.innerHTML}
        <option value="${peli.idMunicipios}">${peli.municipios}</option>
        `;
    });
};

function estadoSel(){
    const contenido = document.getElementById("idPais");
    const contenido2 = document.getElementById("idEstados");
    const contenido3 = document.getElementById("idMunicipios");
    for (let i = contenido2.options.length; i >= 0; i--) {
        contenido2.remove(i);
    }
        for (let i = contenido3.options.length; i >= 0; i--) {
            contenido3.remove(i);
    }
    console.log(contenido2.value);
    fetch(`http://localhost:3000/api/edo/${contenido.value}`)
      .then((response) => response.json())
      .then((data) => {
        console.log(data[0]);
        obtenerPeli(data);
      })
      .catch((error) => {
        console.log(error);
      });
}

function muniSel(){
    const contenido = document.getElementById("idEstados");
    const contenido2 = document.getElementById("idMunicipios");
    for (let i = contenido2.options.length; i >= 0; i--) {
        contenido2.remove(i);
      }
    console.log(contenido2.value);
    fetch(`http://localhost:3000/api/muni/${contenido.value}`)
      .then((response) => response.json())
      .then((data) => {
        console.log(data[0]);
        obtenerPel(data);
      })
      .catch((error) => {
        console.log(error);
      });
}