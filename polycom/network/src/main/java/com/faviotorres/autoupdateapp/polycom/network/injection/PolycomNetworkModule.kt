package com.faviotorres.autoupdateapp.polycom.network.injection

import com.faviotorres.autoupdateapp.polycom.api.PolycomApi
import com.faviotorres.autoupdateapp.polycom.api.constant.Constants
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
abstract class PolycomNetworkModule {

    companion object {

        @Provides
        fun providesMoshi(): Moshi = Moshi.Builder().build()

        @Provides
        fun providesPolycomApiRetrofit(moshi: Moshi): Retrofit =
            Retrofit.Builder()
                .baseUrl(Constants.POLYCOM_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()

        @Provides
        fun providesPolycomApi(retrofit: Retrofit): PolycomApi =
            retrofit.create(PolycomApi::class.java)
    }
}