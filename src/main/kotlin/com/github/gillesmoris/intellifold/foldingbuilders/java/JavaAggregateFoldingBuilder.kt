package com.github.gillesmoris.intellifold.foldingbuilders.java

import com.github.gillesmoris.intellifold.foldingbuilders.AggregateFoldingBuilder

class JavaAggregateFoldingBuilder :
    AggregateFoldingBuilder(arrayOf(JavaRegexFoldingBuilder(), JavaCommentFoldingBuilder()))
