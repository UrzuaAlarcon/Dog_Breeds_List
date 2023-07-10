package AndroidDeveloper.Try.dog_breeds_list

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface APiService {

    @GET
    suspend fun getDogsByBreeds(@Url url:String):Response<DogResponse>

}