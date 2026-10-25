package vn.hust.restaurant.service;
public class SimulatedOtpService implements OtpService{private volatile String lastCode;public void send(String d,String c){lastCode=c;System.out.println("[OTP MÔ PHỎNG] "+d+": "+c);}public String deliveryDescription(){return "OTP mô phỏng (xem console; chế độ dev sẽ hiển thị mã)";}public String lastCode(){return lastCode;}}
