package com.github.gillesmoris.intellifold.foldingbuilders.js

import com.github.gillesmoris.intellifold.foldingbuilders.AggregateFoldingBuilder

class JavaScriptAggregateFoldingBuilder :
    AggregateFoldingBuilder(arrayOf(JavaScriptRegexFoldingBuilder(), JavaScriptCommentFoldingBuilder()))
