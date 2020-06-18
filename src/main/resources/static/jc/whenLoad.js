//данный скрипт реагирует на загрузку, запрашивая аджаксом необходимые параметры.
//секьюрность он не смотрит, ибо раз сюда уже попали, то дошли до контроллера который и рисует страницу
//Выдает
//1. Почиканного принципала для шапки и ченджа видимости вкладки с админом
//2. Список данных для таблицы
$(function () {
    //1. получим информацию о том, кто у нас залогинился
    $.ajax({
        url: '/user/principal',         /* Куда пойдет запрос */
        method: 'get',             /* Метод передачи (post или get) */
        dataType: 'json',          /* Тип данных в ответе (xml, json, script, html). */
        data: {},     /* Параметры передаваемые в запросе. */
        success: function (obj) {   /* функция которая будет выполнена после успешного запроса.  */
            //alert('чет пришло');
            //var obj = JSON.parse(data); не нужно, уже ловим объектом
            //alert('зись в ись :' + obj.login);                 /* В переменной data содержится ответ от index.php. */
            var titleUserNameSpan = $('#titleUserName')
            titleUserNameSpan.text(obj.login)
            var titleUserRolesSpan = $('#titleUserRoles')
            var rolesText = "";
            var isAdmin = false;
            $.each(obj.roles, function (index, value) {
                rolesText = rolesText + ' ' + value;
                if (value == 'Admin') {
                    isAdmin = true;
                }
            });
            titleUserRolesSpan.text(rolesText)

            //2. Выясним где мы
            var href = document.location.href;
            var lastPathSegment = href.substr(href.lastIndexOf('/') + 1);
            var isAdminRequest = lastPathSegment.indexOf('admin') >= 0


            //3. закончили с топом, подправим левую
            var adminRefA = $('#adminRef')
            var userRefA = $('#userRef')
            if (isAdmin) {
                adminRefA.show()
            } else {
                adminRefA.hide()
            }
            if(isAdminRequest) {
                if (!adminRefA.hasClass('active')) {
                    adminRefA.addClass('active')
                }
                if (userRefA.hasClass('active')) {
                    userRefA.removeClass('active')
                }
            } else {
                if (adminRefA.hasClass('active')) {
                    adminRefA.removeClass('active')
                }
                if (!userRefA.hasClass('active')) {
                    userRefA.addClass('active')
                }
            }


            //4 что от нас хотят - юзера инфу, или все данные
            var pathToTableRequest = isAdminRequest ? "/admin/allusers" : "/userInfo"

            $.ajax({
                url: pathToTableRequest,         /* Куда пойдет запрос */
                method: 'get',             /* Метод передачи (post или get) */
                dataType: 'json',          /* Тип данных в ответе (xml, json, script, html). */
                data: {},     /* Параметры передаваемые в запросе. */
                success: function (listUsers) {
                    fillTable($('#userTable'), listUsers)
                },
                error: function (s) {
                    alert("Error")
                },
            });

            //4. подгрузим роли в форму на создание, если нужно
            if (isAdminRequest) {
                var newForm = $('#createForm')
                var newRoleSelect = newForm.find('select#roles')
                fillSelect(newRoleSelect, null, false)
            }
        }
    });

});
