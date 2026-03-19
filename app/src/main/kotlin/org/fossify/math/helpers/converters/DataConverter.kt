package org.fossify.math.helpers.converters

import org.fossify.math.R
import java.math.BigDecimal

object DataConverter : Converter {
    override val nameResId: Int = R.string.unit_data
    override val imageResId: Int = R.drawable.ic_data_vector
    override val key: String = "DataConverter"

    private val BITS_IN_BYTE = BigDecimal("8")
    private val KILO = BigDecimal("1000")
    private val KIBI = BigDecimal("1024")

    sealed class Unit(nameResId: Int, symbolResId: Int, factor: BigDecimal, key: String) :
        Converter.Unit(nameResId, symbolResId, factor, key) {
        data object Bit : Unit(
            nameResId = R.string.unit_data_bit,
            symbolResId = R.string.unit_data_bit_symbol,
            factor = BigDecimal.ONE,
            key = "Bit"
        )

        data object Nibble : Unit(
            nameResId = R.string.unit_data_nibble,
            symbolResId = R.string.unit_data_nibble_symbol,
            factor = BigDecimal("4"),
            key = "Nibble"
        )

        data object Byte : Unit(
            nameResId = R.string.unit_data_byte,
            symbolResId = R.string.unit_data_byte_symbol,
            factor = BITS_IN_BYTE,
            key = "Byte"
        )

        // Decimal Bits (1000^n)
        data object Kilobit : Unit(
            nameResId = R.string.unit_data_kilobit,
            symbolResId = R.string.unit_data_kilobit_symbol,
            factor = KILO,
            key = "Kilobit"
        )

        data object Megabit : Unit(
            nameResId = R.string.unit_data_megabit,
            symbolResId = R.string.unit_data_megabit_symbol,
            factor = KILO.pow(2),
            key = "Megabit"
        )

        data object Gigabit : Unit(
            nameResId = R.string.unit_data_gigabit,
            symbolResId = R.string.unit_data_gigabit_symbol,
            factor = KILO.pow(3),
            key = "Gigabit"
        )

        data object Terabit : Unit(
            nameResId = R.string.unit_data_terabit,
            symbolResId = R.string.unit_data_terabit_symbol,
            factor = KILO.pow(4),
            key = "Terabit"
        )

        data object Petabit : Unit(
            nameResId = R.string.unit_data_petabit,
            symbolResId = R.string.unit_data_petabit_symbol,
            factor = KILO.pow(5),
            key = "Petabit"
        )

        // Binary Bits (1024^n)
        data object Kibibit : Unit(
            nameResId = R.string.unit_data_kibibit,
            symbolResId = R.string.unit_data_kibibit_symbol,
            factor = KIBI,
            key = "Kibibit"
        )

        data object Mebibit : Unit(
            nameResId = R.string.unit_data_mebibit,
            symbolResId = R.string.unit_data_mebibit_symbol,
            factor = KIBI.pow(2),
            key = "Mebibit"
        )

        data object Gibibit : Unit(
            nameResId = R.string.unit_data_gibibit,
            symbolResId = R.string.unit_data_gibibit_symbol,
            factor = KIBI.pow(3),
            key = "Gibibit"
        )

        data object Tebibit : Unit(
            nameResId = R.string.unit_data_tebibit,
            symbolResId = R.string.unit_data_tebibit_symbol,
            factor = KIBI.pow(4),
            key = "Tebibit"
        )

        data object Pebibit : Unit(
            nameResId = R.string.unit_data_pebibit,
            symbolResId = R.string.unit_data_pebibit_symbol,
            factor = KIBI.pow(5),
            key = "Pebibit"
        )

        // Decimal Bytes (8 * 1000^n)
        data object Kilobyte : Unit(
            nameResId = R.string.unit_data_kilobyte,
            symbolResId = R.string.unit_data_kilobyte_symbol,
            factor = BITS_IN_BYTE.multiply(KILO),
            key = "Kilobyte"
        )

        data object Megabyte : Unit(
            nameResId = R.string.unit_data_megabyte,
            symbolResId = R.string.unit_data_megabyte_symbol,
            factor = BITS_IN_BYTE.multiply(KILO.pow(2)),
            key = "Megabyte"
        )

        data object Gigabyte : Unit(
            nameResId = R.string.unit_data_gigabyte,
            symbolResId = R.string.unit_data_gigabyte_symbol,
            factor = BITS_IN_BYTE.multiply(KILO.pow(3)),
            key = "Gigabyte"
        )

        data object Terabyte : Unit(
            nameResId = R.string.unit_data_terabyte,
            symbolResId = R.string.unit_data_terabyte_symbol,
            factor = BITS_IN_BYTE.multiply(KILO.pow(4)),
            key = "Terabyte"
        )

        data object Petabyte : Unit(
            nameResId = R.string.unit_data_petabyte,
            symbolResId = R.string.unit_data_petabyte_symbol,
            factor = BITS_IN_BYTE.multiply(KILO.pow(5)),
            key = "Petabyte"
        )

        // Binary Bytes (8 * 1024^n)
        data object Kibibyte : Unit(
            nameResId = R.string.unit_data_kibibyte,
            symbolResId = R.string.unit_data_kibibyte_symbol,
            factor = BITS_IN_BYTE.multiply(KIBI),
            key = "Kibibyte"
        )

        data object Mebibyte : Unit(
            nameResId = R.string.unit_data_mebibyte,
            symbolResId = R.string.unit_data_mebibyte_symbol,
            factor = BITS_IN_BYTE.multiply(KIBI.pow(2)),
            key = "Mebibyte"
        )

        data object Gibibyte : Unit(
            nameResId = R.string.unit_data_gibibyte,
            symbolResId = R.string.unit_data_gibibyte_symbol,
            factor = BITS_IN_BYTE.multiply(KIBI.pow(3)),
            key = "Gibibyte"
        )

        data object Tebibyte : Unit(
            nameResId = R.string.unit_data_tebibyte,
            symbolResId = R.string.unit_data_tebibyte_symbol,
            factor = BITS_IN_BYTE.multiply(KIBI.pow(4)),
            key = "Tebibyte"
        )

        data object Pebibyte : Unit(
            nameResId = R.string.unit_data_pebibyte,
            symbolResId = R.string.unit_data_pebibyte_symbol,
            factor = BITS_IN_BYTE.multiply(KIBI.pow(5)),
            key = "Pebibyte"
        )
    }

    override val units: List<Unit> = listOf(
        Unit.Bit,
        Unit.Nibble,
        Unit.Byte,
        Unit.Kilobit,
        Unit.Kibibit,
        Unit.Megabit,
        Unit.Mebibit,
        Unit.Gigabit,
        Unit.Gibibit,
        Unit.Terabit,
        Unit.Tebibit,
        Unit.Petabit,
        Unit.Pebibit,
        Unit.Kilobyte,
        Unit.Kibibyte,
        Unit.Megabyte,
        Unit.Mebibyte,
        Unit.Gigabyte,
        Unit.Gibibyte,
        Unit.Terabyte,
        Unit.Tebibyte,
        Unit.Petabyte,
        Unit.Pebibyte,
    )

    override val defaultTopUnit: Unit = Unit.Kilobyte
    override val defaultBottomUnit: Unit = Unit.Byte
}
