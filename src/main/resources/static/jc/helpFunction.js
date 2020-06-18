function fillTable(table, data) {
    var userTableBody = table.find('tbody')[0]// тело таблички пользователей

    //Удалим все предыдущие
    $(userTableBody).children().remove();

    var headCount = table
        .find('thead')[0]
        .getElementsByTagName('tr')[0]
        .getElementsByTagName('th').length //узнали с чем мы работаем
    $.each(data, function (indexs, userDto) {
        //берем следующую строку.
        var row = document.createElement("TR");
        userTableBody.appendChild(row);
        var userid = 0
        // Создаем ячейки в вышесозданной строке
        // и добавляем
        tableKeyList.forEach(function (item, i, keyList) {
            //берем следующий нужный нам ключ
            //берем значение
            for (property in userDto) {
                if (property == item) {
                    var nextValue = userDto[property]
                    if (property == 'id') {
                        userid = nextValue
                    }
                    var innerText = ''
                    if (typeof nextValue != 'object') {
                        innerText = nextValue
                    } else { //значит мы дошли до ролей.
                        $.each(nextValue, function (indexRole, valueRole) {
                            innerText = innerText + ' ' + valueRole
                        })
                    }
                    var td1 = document.createElement("TD");
                    row.appendChild(td1);
                    td1.innerHTML = innerText
                    break //нашли нужное свойство и вставили - пошли за следующим
                }
            }
        });

        if (headCount > 6) { //значит будут созданы еще и кнопки.
            //тогда навесим на них айдишники
            addModalButton(row, 'Edit', false, userid)
            addModalButton(row, 'Delete', true, userid)
            // <button type="button" th:attr="data-whatever=${user.id}" class="btn btn-primary" data-toggle="modal" data-target="#userModal">
            // <button type="button" th:attr="data-whatever=${user.id}" class="btn btn-danger" data-toggle="modal" data-target="#userModal">
        }
    });
}

function addModalButton(rowThatWeAddButton, buttonText, isDeleteButton, dataValue) {
    var button = document.createElement('button')
    $(button).addClass('btn ' + (isDeleteButton ? 'btn-danger' : 'btn-primary'))
    $(button).text(buttonText)
    $(button).attr('data-whatever', dataValue) //прикрутили к какой строке относится
    $(button).attr('data-toggle', 'modal') //прикрутили к какому типу относится
    $(button).attr('data-target', '#userModal') //прикрутили к кому
    var td = document.createElement("TD");
    td.appendChild(button)
    rowThatWeAddButton.appendChild(td);
}

function fillSelect(select, optionNameToSelect, isDeleteMode) {
    select.prop('readonly', isDeleteMode)
    $.ajax({
        url: '/admin/roles',         /* Куда пойдет запрос */
        method: 'get',             /* Метод передачи (post или get) */
        dataType: 'json',          /* Тип данных в ответе (xml, json, script, html). */
        data: {},     /* Параметры передаваемые в запросе. */
        success: function (roles) {
            //итак, нам пришли имена ролей. для каждого.
            //указываем сайз селекта равный сайзу ролей
            select.prop('size', roles.length)
            select.find('option').remove() //очистим от опшнов которые были ранее
            //создаем опшн. Заполняем параметрами.
            $.each(roles, function (index, roleName) {
                var option = document.createElement("option");
                $(option).prop('value', roleName)
                $(option).text(roleName)
                if (optionNameToSelect != null) { //коль данных для селекта нет, о чем речь?
                    $(option).prop('selected', optionNameToSelect.indexOf(roleName) != -1) //значит такая роль есть
                }
                select.append(option);
            })
            //если в массиве роллей есть такой - то еще и селектнем

        }
    })
}

const tableKeyList = [
    "id"
    , "firstName"
    , "lastName"
    , "money"
    , "login"
    , "roles"
]