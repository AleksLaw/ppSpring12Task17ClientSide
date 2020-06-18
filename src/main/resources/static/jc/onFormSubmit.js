$("form").on('submit', function (event) {
    //Итак у нас три варианта.
    // Либо мы тут с созданием
    // Либо с обновлением
    // Либо с удалением
    var submitEvent = event.originalEvent //получили оригинальное событие
    var button = submitEvent.submitter//получили ответственного за это
    var pathToAction = ''
    var httpMethod = ''
    if ($(button).hasClass('btn-success')) {
        pathToAction = '/admin/create'
        httpMethod = 'post'
    } else if ($(button).hasClass('btn-primary')) {
        pathToAction = '/admin/edit'
        httpMethod = 'put'
    } else if ($(button).hasClass('btn-danger')) {
        pathToAction = '/admin/delete'
        httpMethod = 'delete'
    }

    //var data = $(this).serializeToJSON(); удалено. на всякий случай ищи тут https://github.com/raphaelm22/jquery.serializeToJSON


    var formArray = $(this).serializeArray();// От этой идеи пришлось отказаться - совершенно не работает если нет ролей\одна роль

    var myData = {}
    var parentThis = $(this).parent()

    //укладываем обычные инпуты
    var inputs = parentThis.find("input")
    $.each(inputs,function (index, nextInput) {
        var key = nextInput.name
        var value = nextInput.value
        myData[key] = value
    })

    //укладываем все селекты
    var selects = parentThis.find("select")
    $.each(selects, function (index,selectValues) {
            var key = selectValues.name
            myData[key] = new Array() // подготовим поле под опшны селекта
        $.each(selectValues,function (index, nextOption) {
            if(nextOption.selected) { //кладем только те опшны, которые выбраны
                var value = nextOption.value
                myData[key].push(value)
            }
        })
    })
    //здесь можно описать способы укладки остальных типов данных с форм, но опустим их, ибо в данной задаче это не нужно

    //TODO: не забыть включить обратно
    $.ajax({
        type: httpMethod,
        url: pathToAction,
        contentType: "application/json",
        data: JSON.stringify(myData),
        async:false, //чтобы апдейт корректный был
        success: function (e) {
            alert("Finally" + e);
        },
        error: function (s) {
            alert("Error")
        },
    })


    event.preventDefault() // перестраховка, на случай если глупый прогер забыл убрать экшен.
    if (pathToAction != '/admin/create') {
        $("#userModal").modal('hide')
    } else {
        $.ajax({
            url: '/admin/allusers',         /* Куда пойдет запрос. Забито четко, т.к. в иных местах модалок нет*/
            method: 'get',             /* Метод передачи (post или get) */
            dataType: 'json',          /* Тип данных в ответе (xml, json, script, html). */
            data: {},     /* Параметры передаваемые в запросе. */
            success: function (listUsers) {
                fillTable($('#userTable'), listUsers)
            }
        });

        //переключимся между вкладками
        var userListTab = $('#userListTab')
        userListTab.click()
        //и очистим форму.
        $(this)[0].reset()
    }
});
