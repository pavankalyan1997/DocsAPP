package com.example.pk.docsappnitt;

import android.location.Address;

public class Profile {
    public String MobileNumber;
    public String RollNumber;
    public String Address;
    public String Name;
    public String isDoctor;
    public String flagDoctor;
    public String isDiagnostician;
    public String flagDiag;
    public String isPharma;
    public String flagPharma;

    public Profile(String Name,String RollNumber,String MobileNumber,String Address,String isDoctor,String flagDoctor,String isDiagnostician,String flagDiag,
                   String isPharma,String flagPharma){
        this.Name=Name;
        this.Address=Address;
        this.MobileNumber=MobileNumber;
        this.RollNumber=RollNumber;
        this.isDoctor=isDoctor;
        this.flagDoctor=flagDoctor;
        this.isDiagnostician=isDiagnostician;
        this.flagDiag=flagDiag;
        this.isPharma=isPharma;
        this.flagPharma=flagPharma;
    }

    public Profile() {
    }
}
