package us.eventlocations.androidtab.fragments;


public interface OnTutSelectedListener {
    public void onTutSelected(Boolean tutUri);
    public void onTutSelected(String Title);
    public void onCallGPS(String address);
    public void onDialogComingSoon();

}