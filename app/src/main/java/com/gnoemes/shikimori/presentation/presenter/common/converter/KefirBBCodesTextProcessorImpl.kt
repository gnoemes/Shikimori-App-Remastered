package com.gnoemes.shikimori.presentation.presenter.common.converter

import android.content.Context
import com.gnoemes.shikimori.R
import org.kefirsf.bb.BBProcessorFactory
import org.kefirsf.bb.TextProcessor
import javax.inject.Inject

class KefirBBCodesTextProcessorImpl @Inject constructor(
        private val context: Context
) : BBCodesTextProcessor {

    private val processor: TextProcessor  by lazy { BBProcessorFactory.getInstance().create(resource) }
    private val resource by lazy { context.resources.openRawResource(R.raw.shikimori_bb_codes) }

    override fun process(source: CharSequence): CharSequence = processor.process(source)

    override fun process(source: String): String = processor.process(source)
}