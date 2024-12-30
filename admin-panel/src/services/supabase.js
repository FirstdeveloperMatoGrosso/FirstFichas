import { createClient } from '@supabase/supabase-js'

const supabaseUrl = 'https://ilgrwnmvciwwdfcupefr.supabase.co'
const supabaseKey = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImlsZ3J3bm12Y2l3d2RmY3VwZWZyIiwicm9sZSI6InNlcnZpY2Vfcm9sZSIsImlhdCI6MTczNDMyMjIxNywiZXhwIjoyMDQ5ODk4MjE3fQ.aqqCCP2CouVEAYYRUxK78NgwNT_AMqIs1mw-QrQA4pI'

export const supabase = createClient(supabaseUrl, supabaseKey)

// Autenticação
export const signIn = async (email, password) => {
  const { data, error } = await supabase.auth.signInWithPassword({
    email,
    password
  })
  return { data, error }
}

export const signOut = async () => {
  const { error } = await supabase.auth.signOut()
  return { error }
}

// Eventos
export const getEvents = async () => {
  const { data, error } = await supabase
    .from('events')
    .select('*')
    .order('date', { ascending: true })
  return { data, error }
}

export const createEvent = async (event) => {
  const { data, error } = await supabase
    .from('events')
    .insert([event])
    .select()
  return { data, error }
}

export const updateEvent = async (id, updates) => {
  const { data, error } = await supabase
    .from('events')
    .update(updates)
    .eq('id', id)
    .select()
  return { data, error }
}

// Terminais
export const getTerminals = async () => {
  const { data, error } = await supabase
    .from('terminals')
    .select('*')
    .order('created_at', { ascending: false })
  return { data, error }
}

export const updateTerminal = async (id, updates) => {
  const { data, error } = await supabase
    .from('terminals')
    .update(updates)
    .eq('id', id)
    .select()
  return { data, error }
}

// Transações
export const getTransactions = async () => {
  const { data, error } = await supabase
    .from('transactions')
    .select('*, terminals(*)')
    .order('created_at', { ascending: false })
  return { data, error }
}

// Ingressos
export const getTickets = async () => {
  const { data, error } = await supabase
    .from('tickets')
    .select('*, events(*)')
    .order('created_at', { ascending: false })
  return { data, error }
}

export const validateTicket = async (id) => {
  const { data, error } = await supabase
    .from('tickets')
    .update({ 
      status: 'VALIDATED',
      validated_at: new Date()
    })
    .eq('id', id)
    .select()
  return { data, error }
}

// Dashboard Stats
export const getDashboardStats = async () => {
  const today = new Date()
  const thirtyDaysAgo = new Date(today.setDate(today.getDate() - 30))

  // Total de vendas do mês
  const { data: sales } = await supabase
    .from('transactions')
    .select('amount')
    .gte('created_at', thirtyDaysAgo.toISOString())
    .eq('status', 'APPROVED')

  // Total de ingressos vendidos
  const { count: tickets } = await supabase
    .from('tickets')
    .select('*', { count: true })
    .gte('created_at', thirtyDaysAgo.toISOString())

  // Eventos ativos
  const { data: events } = await supabase
    .from('events')
    .select('*')
    .gte('date', new Date().toISOString())

  // Terminais ativos
  const { count: terminals } = await supabase
    .from('terminals')
    .select('*', { count: true })
    .eq('is_active', true)

  return {
    monthlySales: sales?.reduce((acc, curr) => acc + curr.amount, 0) || 0,
    ticketsSold: tickets || 0,
    activeEvents: events?.length || 0,
    activeTerminals: terminals || 0
  }
}

// Relatórios
export const getSalesReport = async (startDate, endDate) => {
  const { data, error } = await supabase
    .from('transactions')
    .select(`
      id,
      amount,
      payment_type,
      status,
      created_at,
      terminals (
        serial,
        model
      )
    `)
    .gte('created_at', startDate)
    .lte('created_at', endDate)
    .order('created_at', { ascending: false })

  return { data, error }
}

export const getEventReport = async (eventId) => {
  const { data, error } = await supabase
    .from('tickets')
    .select(`
      id,
      status,
      validated_at,
      created_at,
      transactions (
        amount,
        payment_type
      )
    `)
    .eq('event_id', eventId)
    .order('created_at', { ascending: false })

  return { data, error }
}
