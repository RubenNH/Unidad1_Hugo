fetch("http://localhost:3000/api/boleto")
    .then((response) => response.json())
    .then((data) => {
        console.log(data);
        obtenerBoleto(data);
    })
    .catch((error) => {
        console.log(error);
    });

fetch("http://localhost:3000/api/funcion")
    .then((response) => response.json())
    .then((data) => {
        console.log(data);
        obtenerPelis(data);
    })
    .catch((error) => {
        console.log(error);
    });



const obtenerPelis = (peliculas) => {
    const contenido = document.getElementById("idFun");
    peliculas.forEach((peli) => {
        contenido.innerHTML = `
            ${contenido.innerHTML}
            <option value="${peli.idFuncion}">${peli.titulo} funcion tipo ${peli.tipo}</option>
            `;
    });
};



const obtenerBoleto = (boletos) => {
    const contenido = document.getElementById("contenido");
    boletos.forEach((boleto) => {
        contenido.innerHTML = `
        ${contenido.innerHTML}
        <tr>
        <td>${boleto.idBoleto}</td>
        <td>${boleto.asientoN}</td>
        <td>${boleto.tipo}</td>
        <td>${boleto.titulo}</td>
        <td>${boleto.costo}</td>
        <td>
        <a href="boletosUpdate.html?id=${boleto.idBoleto}" class="btn btn-warning btn-sm">Editar</a>
        </td>
        <td>
        <a href="boletos.html" onClick="borrar(${boleto.idBoleto})" class="btn btn-danger btn-sm">Eliminar</a>
        </td>
        </tr>
        `;
    });
};
//update


////Convertir form a json
const formElement = document.querySelector("form#createBoleto");
const getFormJSON = (form) => {
    const data = new FormData(form);
    return Array.from(data.keys()).reduce((result, key) => {
        result[key] = data.get(key);
        return result;
    }, {})
}

const handler = (event) => {
    event.preventDefault();
    const valid = formElement.reportValidity();
    if (valid) {
        const result = getFormJSON(formElement);
        console.log(result);
        saveBoleto(result);
    }
}

formElement.addEventListener("submit", handler);
//////////Guardar JSON EN SERVER

const saveBoleto = (data) => {
    fetch("http://localhost:3000/api/boleto/", {
        method: "POST",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data)
    })
        .then((response) => response.json())
        .then((data) => {
            console.log(data);
            parent.location.href = "boletos.html"
        })
        .catch((error) => {
            console.log(error);
            parent.location.href = "boletos.html"
        })
}

function borrar(id) {
    fetch("http://localhost:3000/api/boleto/" + id, {
        method: "DELETE",
        headers: {
            'Content-Type': 'application/json',
        },
    })
}


//UPDATE
/*
const updateUser = document.querySelector("form#updateBoleto");

const getFormJSONUpdate = (form) => {
    const data = new FormData(form);
    return Array.from(data.keys()).reduce((result, key) => {
        result[key] = data.get(key);
        return result;
    }, {})
}

const handlerUpdate = (event) => {
    event.preventDefault();
    const data = getFormJSONUpdate(updateUser);
    console.log(data);
    updateBoleto(data);
  };

  const updateBoleto = (usuario) => {
    const url = new URL(window.location.href);
    const id = url.searchParams.get("id");
    // we gonna send the id in req params
    fetch(`http://localhost:3000/api/boleto/${id}`, {
      method: "PUT",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(usuario),
    })
      .then((response) => response.json())
      .then((data) => {
        console.log(data);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  updateUser.addEventListener("submit", handlerUpdate);

*/








