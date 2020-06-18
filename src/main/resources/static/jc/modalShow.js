$('#userModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget) // Button that triggered the modal
    var userId = button.data('whatever') // Extract info from data-* attributes тут лежит айдшник
    // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
    // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
    var modal = $(this) //т.к. мы нашлись по айдишнику объекта - на него и ссылаемся.
    //выявим, что с режимом
    var isDeleteMode = false
    if (button.hasClass('btn-danger')) {
        isDeleteMode = true
    }
    //подправим тайтл
    var title = modal.find('#userFormTitle')
    title.text(isDeleteMode ? 'Delete user' : 'Edit user') //заменяет содержимое внутри элемента на этот текст, но тут не боимся

    //подправим куда. Отставить. Ловится по событию в onFormSubmit
    // var form = modal.find('#userformid')
    // var formActionPath = isDeleteMode ? ('/removeuser/' + id) : '/edituser/'
    // formActionPath = '/admin' + formActionPath
    // form.prop('action', formActionPath)

    //подправим кнопку
    var submitButton = modal.find('#changeButton')
    submitButton.text(isDeleteMode ? 'Delete' : 'Edit')
    submitButton.removeClass('btn-danger')
    submitButton.removeClass('btn-primary')//перестрахуемся и удалим оба. в случае масштабирования - заменить на удаление всех и вешанье класса btn
    submitButton.addClass(isDeleteMode ? 'btn-danger' : 'btn-primary')

    //создать аккаунт в гугле
    //создать проперти
    //указать зависимость
    //создать бин извлекатор принципала
    //он принимает дао(или только дату?)
    //и в нем содержится карта - проперти от гугла
    //эти проперти мы распихиваем по нашим полям.
    //ищем юзеров по гуглайдишнику. если не нашли - создаем, указываем дефолтный пароль
    //после чего залогиниваем. и перебрасываем.
    var fragmentId = modal.find('#fragmentId')


    $.ajax({
        url: '/admin/edituser/' + userId,         /* Куда пойдет запрос */
        method: 'get',             /* Метод передачи (post или get) */
        dataType: 'json',          /* Тип данных в ответе (xml, json, script, html). */
        data: {},     /* Параметры передаваемые в запросе. */
        success: function (editUserDto) {

            formKeyList.forEach(function (item, i, keyList) {
                //берем следующий нужный нам ключ
                //берем значение
                for (property in editUserDto) {
                    if (property == item) {
                        var nextValue = editUserDto[property]

                        //подготовим текст
                        if (typeof nextValue != 'object') { //значит просто вставим текст
                            if (property == 'password') { //особая обработка для пароля
                                var passworddiv = fragmentId.find('#passworddiv')
                                if (isDeleteMode) {
                                    passworddiv.hide()
                                } else {
                                    passworddiv.show()
                                }
                            }
                            var nextInput = fragmentId.find('input#' + property)
                            nextInput.prop('value', nextValue)
                            nextInput.prop('readonly', isDeleteMode || property == 'id')
                        } else { //значит мы дошли до селектов.
                            nextInput = fragmentId.find('select#' + property)
                            fillSelect(nextInput ,nextValue, isDeleteMode)

                            //проставим всю селектнутость

                        }
                    }
                }
            });
            //погнали обрабатывать. Циклы и селекторы подрубим на следующей задаче

        }
    })

})

const formKeyList = [
    "id"
    , "firstName"
    , "lastName"
    , "money"
    , "login"
    , "password"
    , "roles"
]