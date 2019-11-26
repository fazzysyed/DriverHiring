package fyp.saira.driverhiring.Services;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import fyp.saira.driverhiring.Notifications.MyResponse;
import fyp.saira.driverhiring.Notifications.Sender;
import fyp.saira.driverhiring.Notifications.SenderMessageForRequest;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA1E5jKB8:APA91bF7-Uc0mwaUi_09fMIlF59TZHCLsdqMql8trXqmz7_vSWHxxWuCej5TA21ArvXqnUZJ3-1364mGa00v1x2xWrhkNvnpfPf2nWnpCQu9pXWuHjb0gKW6xQQXQEOdJ0B0MiR6lOlA"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);

    @POST("fcm/send")
    Call<MyResponse> sendRequestNotification(@Body SenderMessageForRequest body);
}
