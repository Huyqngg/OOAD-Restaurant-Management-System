package vn.hust.restaurant.model;

public final class DomainEnums {
    private DomainEnums() {}
    public enum Role { CHU_NHA_HANG, PHUC_VU, DAU_BEP, THU_NGAN }
    public enum TableStatus { TRONG, DA_DAT, DANG_PHUC_VU, CHO_THANH_TOAN, NGUNG_SU_DUNG }
    public enum OrderStatus { MOI, DA_GUI_BEP, DANG_PHUC_VU, CHO_THANH_TOAN, DA_THANH_TOAN, DA_HUY }
    public enum KitchenStatus { CHO_LAM, DANG_LAM, DA_XONG, DA_HUY }
    public enum PaymentMethod { TIEN_MAT, CHUYEN_KHOAN, THE, KET_HOP }
    public enum PromotionType { PHAN_TRAM, SO_TIEN, MON_PHAN_TRAM, MUA_X_TANG_Y }
}
