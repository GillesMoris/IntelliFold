import com.intellij.lang.ASTNode
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet

class DummyAstNode : ASTNode {
    override fun <T : Any?> getUserData(key: Key<T>): T? {
        throw NotImplementedError("DummyAstNode.getUserData not implemented")
    }

    override fun <T : Any?> putUserData(key: Key<T>, value: T?) {
        throw NotImplementedError("DummyAstNode.putUserData not implemented")
    }

    override fun getElementType(): IElementType {
        throw NotImplementedError("DummyAstNode.getElementType not implemented")
    }

    override fun getText(): String {
        throw NotImplementedError("DummyAstNode.getText not implemented")
    }

    override fun getChars(): CharSequence {
        throw NotImplementedError("DummyAstNode.getChars not implemented")
    }

    override fun textContains(c: Char): Boolean {
        throw NotImplementedError("DummyAstNode.textContains not implemented")
    }

    override fun getStartOffset(): Int {
        throw NotImplementedError("DummyAstNode.getStartOffset not implemented")
    }

    override fun getTextLength(): Int {
        throw NotImplementedError("DummyAstNode.getTextLength not implemented")
    }

    override fun getTextRange(): TextRange {
        throw NotImplementedError("DummyAstNode.getTextRange not implemented")
    }

    override fun getTreeParent(): ASTNode {
        throw NotImplementedError("DummyAstNode.getTreeParent not implemented")
    }

    override fun getFirstChildNode(): ASTNode {
        throw NotImplementedError("DummyAstNode.getFirstChildNode not implemented")
    }

    override fun getLastChildNode(): ASTNode {
        throw NotImplementedError("DummyAstNode.getLastChildNode not implemented")
    }

    override fun getTreeNext(): ASTNode {
        throw NotImplementedError("DummyAstNode.getTreeNext not implemented")
    }

    override fun getTreePrev(): ASTNode {
        throw NotImplementedError("DummyAstNode.getTreePrev not implemented")
    }

    override fun getChildren(filter: TokenSet?): Array<ASTNode> {
        throw NotImplementedError("DummyAstNode.getChildren not implemented")
    }

    override fun addChild(child: ASTNode) {
        throw NotImplementedError("DummyAstNode.addChild not implemented")
    }

    override fun addChild(child: ASTNode, anchorBefore: ASTNode?) {
        throw NotImplementedError("DummyAstNode.addChild not implemented")
    }

    override fun addLeaf(leafType: IElementType, leafText: CharSequence, anchorBefore: ASTNode?) {
        throw NotImplementedError("DummyAstNode.addLeaf not implemented")
    }

    override fun removeChild(child: ASTNode) {
        throw NotImplementedError("DummyAstNode.removeChild not implemented")
    }

    override fun removeRange(firstNodeToRemove: ASTNode, firstNodeToKeep: ASTNode?) {
        throw NotImplementedError("DummyAstNode.removeRange not implemented")
    }

    override fun replaceChild(oldChild: ASTNode, newChild: ASTNode) {
        throw NotImplementedError("DummyAstNode.replaceChild not implemented")
    }

    override fun replaceAllChildrenToChildrenOf(anotherParent: ASTNode) {
        throw NotImplementedError("DummyAstNode.replaceAllChildrenToChildrenOf not implemented")
    }

    override fun addChildren(firstChild: ASTNode, firstChildToNotAdd: ASTNode?, anchorBefore: ASTNode?) {
        throw NotImplementedError("DummyAstNode.addChildren not implemented")
    }

    override fun clone(): Any {
        throw NotImplementedError("DummyAstNode.clone not implemented")
    }

    override fun copyElement(): ASTNode {
        throw NotImplementedError("DummyAstNode.copyElement not implemented")
    }

    override fun findLeafElementAt(offset: Int): ASTNode? {
        throw NotImplementedError("DummyAstNode.findLeafElementAt not implemented")
    }

    override fun <T : Any?> getCopyableUserData(key: Key<T>): T? {
        throw NotImplementedError("DummyAstNode.getCopyableUserData not implemented")
    }

    override fun <T : Any?> putCopyableUserData(key: Key<T>, value: T?) {
        throw NotImplementedError("DummyAstNode.putCopyableUserData not implemented")
    }

    override fun findChildByType(type: IElementType): ASTNode? {
        throw NotImplementedError("DummyAstNode.findChildByType not implemented")
    }

    override fun findChildByType(type: IElementType, anchor: ASTNode?): ASTNode? {
        throw NotImplementedError("DummyAstNode.findChildByType not implemented")
    }

    override fun findChildByType(typesSet: TokenSet): ASTNode? {
        throw NotImplementedError("DummyAstNode.findChildByType not implemented")
    }

    override fun findChildByType(typesSet: TokenSet, anchor: ASTNode?): ASTNode? {
        throw NotImplementedError("DummyAstNode.findChildByType not implemented")
    }

    override fun getPsi(): PsiElement {
        throw NotImplementedError("DummyAstNode.getPsi not implemented")
    }

    override fun <T : PsiElement?> getPsi(clazz: Class<T>): T {
        throw NotImplementedError("DummyAstNode.getPsi not implemented")
    }
}
