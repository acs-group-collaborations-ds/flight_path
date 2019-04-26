package com.example.orbitalflightpaths;

class FlightCalculator {
    private static float uPrimary = 1.32715e+20f;
    private static float parkingOrbitAltitude = 300000;


    FlightCalculator(){

    }

    protected float SemiMajorAxis(float orbitradiusS, float orbitRadiusD){
        return (orbitradiusS + orbitRadiusD) / 2;
    }

    protected float OrbitVelocity(float orbitRadius){
        return (float)Math.sqrt(uPrimary * orbitRadius);
    }

    protected float Velocity(float orbitRadius, float semiMajorAxis){
        return (float)Math.sqrt(uPrimary * ((2 / orbitRadius) - (1 / semiMajorAxis)));
    }

    protected float VelocityInf(float velocity, float orbitVelocity){
        return Math.abs(velocity - orbitVelocity);
    }

    protected float uS(float planetMass){
        return (6.674e-11f) * planetMass;
    }

    protected float ParkingOrbitRadius(float planetRadius){
        return planetRadius + parkingOrbitAltitude;
    }

    protected float ParkingOrbitCircularVel(float uS, float parkingOrbitRadius){
        return (float)Math.sqrt(uS/parkingOrbitRadius);
    }

    protected float VelocityEscape(float uS, float parkingOrbitRadius){
        return (float)Math.sqrt((2 * uS)/parkingOrbitRadius);
    }

    protected float VelocityHyper(float velocityInf, float velocityEsc){
        return (float)Math.sqrt((velocityInf *velocityInf) + (velocityEsc *velocityEsc));
    }

    protected float DeltaV(float velocityHyper, float parkingOrbitCircularVel){
        return velocityHyper - parkingOrbitCircularVel;
    }
    protected float FinalDeltaV(float deltavS, float deltavD){
        return (Math.abs(deltavS) + Math.abs(deltavD));
    }
}
