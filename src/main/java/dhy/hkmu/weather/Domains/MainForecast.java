package dhy.hkmu.weather.Domains;

public class MainForecast {
    public String day;
    public String temp;
    public String picPath;

    public MainForecast(String day, String temp, String picPath) {
        this.day = day;
        this.temp = temp;
        this.picPath = picPath;
    }


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
