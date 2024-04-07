package dhy.hkmu.weather.Domains;

public class SearchForecast {
    public String dayOfWeek;
    public String forecastTemp;
    public String picPath;
    public String forecastDescription;

    public SearchForecast(String dayOfWeek, String forecastTemp, String picPath,String forecastDescription) {
        this.dayOfWeek=dayOfWeek;
        this.forecastTemp=forecastTemp;
        this.picPath=picPath;
        this.forecastDescription=forecastDescription;
    }


    public String getDay() {
        return dayOfWeek;
    }

    public void setDay(String day) {
        this.dayOfWeek = day;
    }

    public String getTemp() {
        return forecastTemp;
    }

    public String getForecastDescription() {
        return forecastDescription;
    }

    public void setForecastDescription(String forecastDescription) {
        this.forecastDescription = forecastDescription;
    }

    public void setTemp(String temp) {
        this.forecastTemp = temp;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
