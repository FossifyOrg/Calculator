package org.fossify.math.helpers.converters

import org.fossify.math.R
import java.math.BigDecimal

/***
 * Base unit: joule.
 *
 * References:
 * - https://en.wikipedia.org/wiki/Joule
 * - https://en.wikipedia.org/wiki/Calorie
 * - https://en.wikipedia.org/wiki/Kilowatt-hour
 * - https://en.wikipedia.org/wiki/Electronvolt
 * - https://en.wikipedia.org/wiki/British_thermal_unit
 * - https://en.wikipedia.org/wiki/Foot-pound_(energy)
 * - https://en.wikipedia.org/wiki/Erg
 * - https://en.wikipedia.org/wiki/Therm
 */
object EnergyConverter : Converter {
    override val nameResId: Int = R.string.unit_energy
    override val imageResId: Int = R.drawable.ic_energy_vector
    override val key: String = "EnergyConverter"

    sealed class Unit(nameResId: Int, symbolResId: Int, factor: BigDecimal, key: String) :
        Converter.Unit(nameResId, symbolResId, factor, key) {
        data object Joule : Unit(
            nameResId = R.string.unit_energy_joule,
            symbolResId = R.string.unit_energy_joule_symbol,
            factor = BigDecimal.ONE,
            key = "Joule"
        )

        data object Kilojoule : Unit(
            nameResId = R.string.unit_energy_kilojoule,
            symbolResId = R.string.unit_energy_kilojoule_symbol,
            factor = BigDecimal("1000"),
            key = "Kilojoule"
        )

        data object Megajoule : Unit(
            nameResId = R.string.unit_energy_megajoule,
            symbolResId = R.string.unit_energy_megajoule_symbol,
            factor = BigDecimal("1000000"),
            key = "Megajoule"
        )

        data object Gigajoule : Unit(
            nameResId = R.string.unit_energy_gigajoule,
            symbolResId = R.string.unit_energy_gigajoule_symbol,
            factor = BigDecimal("1000000000"),
            key = "Gigajoule"
        )

        data object Calorie : Unit(
            nameResId = R.string.unit_energy_calorie,
            symbolResId = R.string.unit_energy_calorie_symbol,
            factor = BigDecimal("4.184"),
            key = "Calorie"
        )

        data object Kilocalorie : Unit(
            nameResId = R.string.unit_energy_kilocalorie,
            symbolResId = R.string.unit_energy_kilocalorie_symbol,
            factor = BigDecimal("4184"),
            key = "Kilocalorie"
        )

        data object WattHour : Unit(
            nameResId = R.string.unit_energy_watt_hour,
            symbolResId = R.string.unit_energy_watt_hour_symbol,
            factor = BigDecimal("3600"),
            key = "WattHour"
        )

        data object KilowattHour : Unit(
            nameResId = R.string.unit_energy_kilowatt_hour,
            symbolResId = R.string.unit_energy_kilowatt_hour_symbol,
            factor = BigDecimal("3600000"),
            key = "KilowattHour"
        )

        data object MegawattHour : Unit(
            nameResId = R.string.unit_energy_megawatt_hour,
            symbolResId = R.string.unit_energy_megawatt_hour_symbol,
            factor = BigDecimal("3600000000"),
            key = "MegawattHour"
        )

        data object Electronvolt : Unit(
            nameResId = R.string.unit_energy_electronvolt,
            symbolResId = R.string.unit_energy_electronvolt_symbol,
            factor = BigDecimal("1.602176634E-19"),
            key = "Electronvolt"
        )

        data object BritishThermalUnit : Unit(
            nameResId = R.string.unit_energy_btu,
            symbolResId = R.string.unit_energy_btu_symbol,
            factor = BigDecimal("1055.05585262"),
            key = "BritishThermalUnit"
        )

        data object Therm : Unit(
            nameResId = R.string.unit_energy_therm,
            symbolResId = R.string.unit_energy_therm_symbol,
            factor = BigDecimal("105505585.262"),
            key = "Therm"
        )

        data object FootPound : Unit(
            nameResId = R.string.unit_energy_foot_pound,
            symbolResId = R.string.unit_energy_foot_pound_symbol,
            factor = BigDecimal("1.3558179483314004"),
            key = "FootPound"
        )

        data object Erg : Unit(
            nameResId = R.string.unit_energy_erg,
            symbolResId = R.string.unit_energy_erg_symbol,
            factor = BigDecimal("0.0000001"),
            key = "Erg"
        )
    }

    override val units: List<Unit> = listOf(
        Unit.Joule,
        Unit.Kilojoule,
        Unit.Megajoule,
        Unit.Gigajoule,
        Unit.Calorie,
        Unit.Kilocalorie,
        Unit.WattHour,
        Unit.KilowattHour,
        Unit.MegawattHour,
        Unit.Electronvolt,
        Unit.BritishThermalUnit,
        Unit.Therm,
        Unit.FootPound,
        Unit.Erg,
    )

    override val defaultTopUnit: Unit = Unit.Kilocalorie
    override val defaultBottomUnit: Unit = Unit.Kilojoule
}
