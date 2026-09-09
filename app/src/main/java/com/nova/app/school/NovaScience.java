package com.nova.app.school;

import android.content.Context;
import java.util.Arrays;
import java.util.List;

public final class NovaScience {

    private NovaScience() {}

    public static List<String> subjects() {
        return Arrays.asList(
                "Physics",
                "Chemistry",
                "Biology"
        );
    }

    public static List<String> physicsLessons() {
        return Arrays.asList(
                "Measurements",
                "Scalar and Vector Quantities",
                "Length, Time, Mass and Weight",
                "Volume and Density",
                "Centre of Gravity",
                "Distance and Displacement",
                "Speed and Velocity",
                "Acceleration",
                "Motion Graphs",
                "Force",
                "Collisions and Momentum",
                "Heat and Thermometry",
                "Transfer of Thermal Energy",
                "Waves and Sound",
                "Light and Refraction",
                "Electricity",
                "Magnetism and Electromagnetism",
                "X-rays",
                "Radioactivity",
                "Nuclear Fission and Fusion"
        );
    }

    public static List<String> chemistryLessons() {
        return Arrays.asList(
                "Particle Theory and States of Matter",
                "Elements, Compounds and Mixtures",
                "Atomic Structure",
                "Periodic Table",
                "Chemical Bonding",
                "Formulae and Equations",
                "Acids, Bases and Indicators",
                "Salts and Preparation",
                "Rates of Reaction",
                "Energy Changes in Reactions",
                "Air and Combustion",
                "Water",
                "Metals and Reactivity",
                "Carbon and Organic Chemistry",
                "Fuels and Petroleum",
                "Environmental Chemistry"
        );
    }

    public static List<String> biologyLessons() {
        return Arrays.asList(
                "Characteristics of Living Organisms",
                "Cell Structure and Organisation",
                "Movement of Substances",
                "Nutrition in Plants",
                "Nutrition in Humans",
                "Respiration",
                "Excretion",
                "Coordination and Response",
                "Reproduction",
                "Growth and Development",
                "Transport in Plants",
                "Transport in Humans",
                "Ecology",
                "Inheritance and Variation",
                "Microorganisms and Disease",
                "Human Health and Conservation"
        );
    }

    public static String teach(String subject, String topic) {

        String s = subject == null
                ? ""
                : subject.toLowerCase();

        String t = topic == null
                ? ""
                : topic.toLowerCase();

        /*
         * SOURCE-BASED PHYSICS
         * Primary teaching reference:
         * supplied O LEVEL PHYSICS NOTES.pdf
         * and supplied Ace Education Physics O'Level PDF.
         */

        if (s.contains("physics")) {

            if (t.contains("measurement")) {
                return
                        "PDF SOURCE LESSON — MEASUREMENTS\n\n"
                        + "Physics deals with matter, energy and their interactions.\n\n"
                        + "Measurement means finding the value of a physical quantity "
                        + "using a scientific instrument with a standard scale.\n\n"
                        + "The supplied Physics notes identify seven fundamental quantities:\n"
                        + "• Length — metre (m)\n"
                        + "• Mass — kilogram (kg)\n"
                        + "• Time — second (s)\n"
                        + "• Thermodynamic temperature — kelvin (K)\n"
                        + "• Electric current — ampere (A)\n"
                        + "• Amount of substance — mole (mol)\n"
                        + "• Luminous intensity — candela (cd)\n\n"
                        + "In mechanics, length, mass and time are fundamental quantities.\n\n"
                        + "STUDY FOCUS:\n"
                        + "Physical quantities, SI units, measuring instruments and "
                        + "accurate recording of measurements.";
            }

            if (t.contains("momentum")
                    || t.contains("collision")) {
                return
                        "PDF SOURCE LESSON — COLLISIONS AND MOMENTUM\n\n"
                        + "Linear momentum is the product of mass and velocity.\n\n"
                        + "Impulse is the change in momentum and is also the product "
                        + "of force and the time of impact.\n\n"
                        + "Momentum and impulse are vector quantities.\n\n"
                        + "The supplied notes state the principle of conservation "
                        + "of momentum: when bodies collide, total momentum remains "
                        + "constant provided no external force acts.\n\n"
                        + "For collision calculations use:\n"
                        + "Total momentum before = Total momentum after.\n\n"
                        + "PRACTICE FOCUS:\n"
                        + "Mass, velocity, momentum, impulse and collision calculations.";
            }

            if (t.contains("heat")
                    || t.contains("thermometry")
                    || t.contains("thermal")) {
                return
                        "PDF SOURCE LESSON — HEAT AND THERMAL PROPERTIES\n\n"
                        + "The supplied notes describe heat as a form of energy "
                        + "associated with the random movement of molecules.\n\n"
                        + "Temperature is the degree of hotness or coldness of a body.\n\n"
                        + "Thermometers use thermometric properties that change "
                        + "with temperature. Examples include changes in length, "
                        + "potential difference, volume and pressure.\n\n"
                        + "The notes cover Celsius, Fahrenheit and Kelvin scales "
                        + "and the fixed points used when establishing a thermometer scale.\n\n"
                        + "STUDY FOCUS:\n"
                        + "Temperature scales, fixed points, thermometric properties "
                        + "and thermal energy transfer.";
            }

            if (t.contains("light")
                    || t.contains("refraction")
                    || t.contains("prism")) {
                return
                        "PDF SOURCE LESSON — LIGHT AND REFRACTION\n\n"
                        + "The supplied Physics material covers refraction, "
                        + "Snell's law, refractive index, prisms, critical angle "
                        + "and total internal reflection.\n\n"
                        + "Total internal reflection occurs when light travels "
                        + "from a denser medium to a less dense medium and the "
                        + "angle of incidence is greater than the critical angle.\n\n"
                        + "STUDY FOCUS:\n"
                        + "Laws of refraction, refractive index, prism calculations, "
                        + "critical angle and total internal reflection.";
            }

            if (t.contains("electricity")
                    || t.contains("motor")
                    || t.contains("magnet")) {
                return
                        "PDF SOURCE LESSON — ELECTRICITY AND ELECTROMAGNETISM\n\n"
                        + "The supplied Physics notes cover electrical energy, "
                        + "power, the kilowatt-hour, electrical cost calculations, "
                        + "magnetic effects of current and the operation of a d.c. motor.\n\n"
                        + "In the d.c. motor, forces acting on the current-carrying "
                        + "coil form a couple which produces rotation. The commutator "
                        + "reverses the current so the rotation continues.\n\n"
                        + "STUDY FOCUS:\n"
                        + "Current, resistance, power, electrical energy, magnetic fields, "
                        + "Fleming's left-hand rule and motor action.";
            }

            if (t.contains("radio")
                    || t.contains("nuclear")
                    || t.contains("fission")
                    || t.contains("fusion")) {
                return
                        "PDF SOURCE LESSON — RADIOACTIVITY AND NUCLEAR PHYSICS\n\n"
                        + "Radioactivity is the spontaneous disintegration of "
                        + "heavy unstable nuclei accompanied by radiation.\n\n"
                        + "The supplied notes cover alpha, beta and gamma radiation, "
                        + "radioactive decay, half-life, nuclear fission and nuclear fusion.\n\n"
                        + "Fission involves the splitting of a heavy nucleus.\n"
                        + "Fusion involves combining light nuclei at very high temperatures.\n\n"
                        + "STUDY FOCUS:\n"
                        + "Radiation properties, uses, decay equations, half-life, "
                        + "fission, fusion and nuclear energy.";
            }

            return
                    "PDF SOURCE LESSON — O-LEVEL PHYSICS\n\n"
                    + "This topic is part of the supplied O-Level Physics "
                    + "learning material.\n\n"
                    + "NOVA will teach the definition, principles, examples, "
                    + "calculations and applications for this topic at "
                    + "Ordinary Level.";
        }

        if (s.contains("chemistry")) {
            return
                    "O-LEVEL CHEMISTRY — " + topic + "\n\n"
                    + "Learn the definitions, formulae, symbols, equations, "
                    + "observations, practical methods and applications required "
                    + "at Ordinary Level.\n\n"
                    + "After the lesson, NOVA provides practice and an adaptive assessment.";
        }

        if (s.contains("biology")) {
            return
                    "O-LEVEL BIOLOGY — " + topic + "\n\n"
                    + "Learn the biological definitions, processes, labelled "
                    + "structures, experiments, applications and explanations "
                    + "required at Ordinary Level.\n\n"
                    + "After the lesson, NOVA provides practice and an adaptive assessment.";
        }

        return "SCIENCE LESSON — " + topic;
    }

    public static void markCompleted(
            Context context,
            String subject,
            String topic
    ) {
        android.content.SharedPreferences p =
                context.getSharedPreferences(
                        "nova_science_progress",
                        Context.MODE_PRIVATE
                );

        int count = p.getInt("completed", 0);

        p.edit()
                .putInt("completed", count + 1)
                .putString("last_subject", subject)
                .putString("last_topic", topic)
                .apply();
    }

    public static int completedCount(Context context) {
        return context.getSharedPreferences(
                "nova_science_progress",
                Context.MODE_PRIVATE
        ).getInt("completed", 0);
    }
}
