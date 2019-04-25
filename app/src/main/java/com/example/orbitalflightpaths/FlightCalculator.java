package com.example.orbitalflightpaths;

class FlightCalculator {
    private static double uPrimary = 1.32715e+20;
    private static double parkingOrbitAltitude = 300000;


    FlightCalculator(){

    }

    protected double SemiMajorAxis(double orbitradiusS, double orbitRadiusD){
        return (orbitradiusS + orbitRadiusD) / 2;
    }

    protected double OrbitVelocity(double orbitRadius){
        return Math.sqrt(uPrimary * orbitRadius);
    }

    protected double Velocity(double orbitRadius, double semiMajorAxis){
        return Math.sqrt(uPrimary * ((2 / orbitRadius) - (1 / semiMajorAxis)));
    }

    protected double VelocityInf(double velocity, double orbitVelocity){
        return Math.abs(velocity - orbitVelocity);
    }

    protected double uS(double planetMass){
        return (6.674e-11) * planetMass;
    }

    protected double ParkingOrbitRadius(double planetRadius){
        return planetRadius + parkingOrbitAltitude;
    }

    protected double ParkingOrbitCircularVel(double uS, double parkingOrbitRadius){
        return Math.sqrt(uS/parkingOrbitRadius);
    }

    protected double VelocityEscape(double uS, double parkingOrbitRadius){
        return Math.sqrt((2 * uS)/parkingOrbitRadius);
    }

    protected double VelocityHyper(double velocityInf, double velocityEsc){
        return Math.sqrt((velocityInf *velocityInf) + (velocityEsc *velocityEsc));
    }

    protected double DeltaV(double velocityHyper, double parkingOrbitCircularVel){
        return velocityHyper - parkingOrbitCircularVel;
    }
    protected double FinalDeltaV(double deltavS, double deltavD){
        return Math.abs(deltavS) + Math.abs(deltavD);
    }
}
