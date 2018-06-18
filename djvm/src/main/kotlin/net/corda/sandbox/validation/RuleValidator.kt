package net.corda.sandbox.validation

import net.corda.sandbox.analysis.AnalysisConfiguration
import net.corda.sandbox.analysis.ClassAndMemberVisitor
import net.corda.sandbox.code.EmitterModule
import net.corda.sandbox.code.Instruction
import net.corda.sandbox.references.Class
import net.corda.sandbox.references.Member
import net.corda.sandbox.rules.ClassRule
import net.corda.sandbox.rules.InstructionRule
import net.corda.sandbox.rules.MemberRule
import net.corda.sandbox.rules.Rule
import net.corda.sandbox.utilities.Processor
import org.objectweb.asm.ClassVisitor

/**
 * Helper class for validating a set of rules for a class or set of classes.
 *
 * @property rules A set of rules to validate for provided classes.
 * @param classVisitor Class visitor to use when traversing the structure of classes.
 */
class RuleValidator(
        private val rules: List<Rule> = emptyList(),
        configuration: AnalysisConfiguration = AnalysisConfiguration(),
        classVisitor: ClassVisitor? = null
) : ClassAndMemberVisitor(classVisitor, configuration = configuration) {

    /**
     * Apply the set of rules to the traversed class and record any violations.
     */
    override fun visitClass(clazz: Class): Class {
        if (shouldBeProcessed(clazz.name)) {
            val context = RuleContext(currentAnalysisContext())
            Processor.processEntriesOfType<ClassRule>(rules, analysisContext.messages) {
                it.validate(context, clazz)
            }
        }
        return super.visitClass(clazz)
    }

    /**
     * Apply the set of rules to the traversed method and record any violations.
     */
    override fun visitMethod(clazz: Class, method: Member): Member {
        if (shouldBeProcessed(clazz.name) && shouldBeProcessed(method.reference)) {
            val context = RuleContext(currentAnalysisContext())
            Processor.processEntriesOfType<MemberRule>(rules, analysisContext.messages) {
                it.validate(context, method)
            }
        }
        return super.visitMethod(clazz, method)
    }

    /**
     * Apply the set of rules to the traversed field and record any violations.
     */
    override fun visitField(clazz: Class, field: Member): Member {
        if (shouldBeProcessed(clazz.name) && shouldBeProcessed(field.reference)) {
            val context = RuleContext(currentAnalysisContext())
            Processor.processEntriesOfType<MemberRule>(rules, analysisContext.messages) {
                it.validate(context, field)
            }
        }
        return super.visitField(clazz, field)
    }

    /**
     * Apply the set of rules to the traversed instruction and record any violations.
     */
    override fun visitInstruction(method: Member, emitter: EmitterModule, instruction: Instruction) {
        if (shouldBeProcessed(method.className) && shouldBeProcessed(method.reference)) {
            val context = RuleContext(currentAnalysisContext())
            Processor.processEntriesOfType<InstructionRule>(rules, analysisContext.messages) {
                it.validate(context, instruction)
            }
        }
        super.visitInstruction(method, emitter, instruction)
    }

}