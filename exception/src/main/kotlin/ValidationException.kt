class ValidationException(val errorCode: Array<ErrorCode>) : RuntimeException(errorCode.joinToString(",") { it.msg })

enum class ErrorCode(val code: Int, val msg: String) {
    // Ошибки заполнения имени/фамилии
    NAME_INVALID_CHARACTER(100, "Недопустимый символ в имени/фамилии"),
    NAME_INVALID_SIZE(101, "Некорректная длина имени/фамилии"),

    // Ошибки заполнения номера телефона
    PHONE_INVALID_CHARACTER(200, "Недопустимый символ в номере телефона"),
    PHONE_INVALID_SIZE(201, "Некорректная длина номера телефона"),

    // Ошибки заполнения e-mail
    EMAIL_INVALID_CHARACTER(300, "Недопустимый адрес e-mail"),
    EMAIL_INVALID_SIZE(301, "Некорректная длина адреса e-mail"),

    // Ошибки заполнения СНИЛС
    SNILS_INVALID_CHARACTER(400, "Недопустимый символ в номере СНИЛС"),
    SNILS_INVALID_SIZE(401, "Некорректная длина номера СНИЛС"),
    SNILS_INVALID_CONTROL_NUMBER(402, "Некорректное контрольное число СНИЛС"),

    // Прочие ошибки
    NULL_OR_EMPTY_VALUE(500, "Отсутствует переданное значение")
}