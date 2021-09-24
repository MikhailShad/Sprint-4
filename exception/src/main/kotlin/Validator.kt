abstract class Validator<T> {
    protected val rules = mutableListOf<(T) -> ErrorCode?>()
    abstract fun validate(value: T?): List<ErrorCode>
}

abstract class ClientStringFieldValidator() : Validator<String>() {

    final override fun validate(value: String?): List<ErrorCode> {
        if (value.isNullOrBlank()) {
            return listOf(ErrorCode.NULL_OR_EMPTY_VALUE)
        }

        return rules.asSequence()
            .map { rule -> rule(value) }
            .filter { it != null }
            .map { it!! }
            .distinct()
            .toList()
    }

}

object NameValidator : ClientStringFieldValidator() {
    private const val MAX_SIZE = 16
    private const val REGEX_TEMPLATE = "[А-Я][а-я]*"

    init {
        rules.add { name -> if (name.length > MAX_SIZE) ErrorCode.NAME_INVALID_SIZE else null }
        rules.add { name -> if (!name.matches(REGEX_TEMPLATE.toRegex())) ErrorCode.NAME_INVALID_CHARACTER else null }
    }

}

object PhoneValidator : ClientStringFieldValidator() {
    private const val EXACT_SIZE = 11
    private const val REGEX_TEMPLATE = "[78]\\d{10}"

    init {
        rules.add { phone -> if (phone.length != EXACT_SIZE) ErrorCode.PHONE_INVALID_SIZE else null }
        rules.add { phone -> if (!phone.matches(REGEX_TEMPLATE.toRegex())) ErrorCode.PHONE_INVALID_CHARACTER else null }
    }

}

object EmailValidator : ClientStringFieldValidator() {
    private const val MAX_SIZE = 32
    private const val REGEX_TEMPLATE = "[A-Za-z]+@[A-Za-z]+.[A-Za-z]+"

    init {
        rules.add { email -> if (email.length > MAX_SIZE) ErrorCode.EMAIL_INVALID_SIZE else null }
        rules.add { email -> if (!email.matches(REGEX_TEMPLATE.toRegex())) ErrorCode.EMAIL_INVALID_CHARACTER else null }
    }

}

object SnilsValidator : ClientStringFieldValidator() {
    private const val EXACT_SIZE = 11
    private const val REGEX_TEMPLATE = "\\d{11}"

    init {
        rules.add { snils -> if (snils.length != EXACT_SIZE) ErrorCode.SNILS_INVALID_SIZE else null }
        rules.add { snils ->
            if (!snils.matches(REGEX_TEMPLATE.toRegex())) {
                return@add ErrorCode.SNILS_INVALID_CHARACTER
            }

            val controlNumber = snils.substring(9).toInt()
            val controlSum = snils.substring(0..9)
                .reversed()
                .foldIndexed(0) { index, sum, value -> sum + index * Character.getNumericValue(value) }

            if (controlSum % 101 % 100 != controlNumber) {
                return@add ErrorCode.SNILS_INVALID_CONTROL_NUMBER
            }

            return@add null
        }
    }

}