/*
 * Copyright (C) 2026  PranavSagarXD
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.sagar.momento.billing.data

import com.revenuecat.purchases.CacheFetchPolicy
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.awaitCustomerInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Single
import com.sagar.momento.billing.domain.BillingHandler
import com.sagar.momento.billing.domain.SubscriptionResult

@Single(binds = [BillingHandler::class])
class BillingHandlerImpl : BillingHandler {
    private val purchases by lazy { Purchases.sharedInstance }

    override suspend fun isPlusUser(): Boolean {
        return userResult() is SubscriptionResult.Subscribed
    }

    override suspend fun userResult(): SubscriptionResult {
        try {
            val userInfo =
                withContext(Dispatchers.IO) {
                    purchases.awaitCustomerInfo(
                        fetchPolicy = CacheFetchPolicy.NOT_STALE_CACHED_OR_CURRENT
                    )
                }
            val entitlement = userInfo.entitlements.all[ENTITLEMENT_PLUS]
            val isPlus = entitlement?.isActive
            if (isPlus == true) {
                return SubscriptionResult.Subscribed
            }
        } catch (e: Exception) {
            return SubscriptionResult.Error(e)
        }

        return SubscriptionResult.NotSubscribed
    }

    companion object {
        private const val ENTITLEMENT_PLUS = "plus"
    }
}
