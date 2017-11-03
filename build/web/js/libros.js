/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var tabla = $('#tb-libros').DataTable({
    ajax: {
        url: "ConsultarLibros",
        dataSrc: ""
    },
    columns: [
        {
            data: "clave"
        },
        {
            data: "nombre"
        },
        {
            data: "autor"
        },
        {
            data: function (row) {
                let str = "";
                if (row["estado"]) {
                    str = "<button class='btn btn-success btn-sm' onclick='updateEstado(" + row["clave"] + ")'>DISPONIBLE</button>"
                } else {
                    str = "<button class='btn btn-danger btn-sm' onclick='updateEstado(" + row["clave"] + ")'>PRESTADO</button>"
                }
                return str;
            }
        },
    ]
});

function updateEstado(clave) {
    $.ajax({
        url: "ActualizarEstado",
        type: "post",
        data: {
            clave: clave,
        }
    }).done(function (data) {
        console.log(data);
        switch (data.code) {
            case 200:
                tabla.ajax.reload(null, false);
                break;
            default:
                swal("ATENCION!", data.message, "error");
        }
    }).fail(function (data) {
        console.log(data);
        swal("INFORMACION!", "Oops! Es que no me tienen paciencia", "error");
    });
}

$("#form-register").validate({
    rules: {
        nombre: {//name del input
            required: true
        },
        autor: {//name del input
            required: true
        },
    },
    messages: {
        nombre: {
            required: "<strong>*Este campo es obligatorio</strong>"
        },
        autor: {
            required: "<strong>*Este campo es obligatorio</strong>"
        },
    },
    highlight: function (element) {    //todos los eementos de color rojo
        //el elemento que este más cerca de form-group le ponga la clase de error
        $(element).closest(".form-group").addClass("has-error");
    },
    unhighlight: function (element) {  //quitar el color rojo
        $(element).closest(".form-group").removeClass("has-error");
    },

    //elementos para poner el mensaje
    errorElement: "p", //para ponerlo en un <p>
    errorClass: "text-danger",
    errorPlacement: function (error, element) {
        error.insertAfter(element.parent());    //insertar despues del padre
    },
    //cuando todo esta bien
    submitHandler: function (form) {
        console.log("Hey hey hey algo bien siempre al 100 pal dj everyday..");
        nuevoLibro();
        return false; //evito que haga el submit. 
    }
});

function nuevoLibro() {
    $.ajax({
        url: "CrearLibro",
        type: "post",
        data: {
            nombre: $("#nombre").val(),
            autor: $("#autor").val()
        }
    }).done(function (data) {
        console.log(data);
        switch (data.code) {
            case 200:
                tabla.ajax.reload(null, false);
                break;
            default:
                swal("ATENCION!", data.message, "error");
        }
    }).fail(function (data) {
        console.log(data);
        swal("INFORMACION!", "Oops! Es que no me tienen paciencia", "error");
    });
}